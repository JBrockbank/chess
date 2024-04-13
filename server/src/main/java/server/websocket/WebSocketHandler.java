package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;

import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final SQLGameDAO gameDAO;
    private final SQLAuthDAO authDAO;

    public WebSocketHandler() {
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.print("Server.WebSocketHandler.onMessage called");
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinGame(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case LEAVE -> leaveGame(message, session);
            case REDRAW -> redraw(message, session);
            case MAKE_MOVE -> makeMove(message, session);
            case RESIGN -> resign(message, session);

        }
    }




    public void joinGame(String action, Session session) {
        JoinPlayer joinPlayer = new Gson().fromJson(action, JoinPlayer.class);

        String authToken = joinPlayer.getAuthToken();
        ChessGame.TeamColor color = joinPlayer.getColor();
        int gameID = joinPlayer.getID();
        connections.add(gameID, authToken, session);
        try {

            GameData game = gameDAO.getGame(gameID);


            AuthData auth = authDAO.getAuth(authToken);
            String userName = auth.username();

            if (!Objects.equals(userName, game.whiteUsername()) && !Objects.equals(userName, game.blackUsername())){
                Error error = new Error("Need to call http first");
                connections.sendMessage(gameID, authToken, new Gson().toJson(error));
                return;
            }



            if (color != ChessGame.TeamColor.WHITE && color != ChessGame.TeamColor.BLACK) {
                Error error = new Error("No color");
                connections.sendMessage(gameID, authToken, new Gson().toJson(error));
                return;
            }


            if (game.whiteUsername() != null && color == ChessGame.TeamColor.WHITE && !(userName.equals(game.whiteUsername()))){
                Error error = new Error("Spot already taken");
                String message = new Gson().toJson(error);
                connections.sendMessage(gameID, authToken, message);
                return;
            }
            else if (game.blackUsername()!= null && color == ChessGame.TeamColor.BLACK && !(userName.equals(game.blackUsername()))){

                Error error = new Error("Spot already taken");
                connections.sendMessage(gameID, authToken, new Gson().toJson(error));
                return;
            }

            var message = String.format(userName + " has joined team " + color);
            var notification = new Notification(message);

            connections.add(gameID, authToken, session);
            connections.broadcast(authToken, notification, gameID);

            var loadGame = new LoadGame(game);
            connections.sendMessage(gameID, authToken, new Gson().toJson(loadGame));
        } catch (Exception e) {
            Error error = new Error("Error");
            connections.sendMessage(gameID, authToken, new Gson().toJson(error));
        }


    }


    public void joinObserver(String action, Session session) {
        JoinObserver joinObserver = new Gson().fromJson(action, JoinObserver.class);
        String authToken = joinObserver.getAuthToken();
        int gameID = joinObserver.getGameID();
        connections.add(gameID, authToken, session);
        try {
            authenticateToken(authToken);
            GameData gameData = gameDAO.getGame(gameID);
            String userName = getUsername(authToken);

            var message = String.format(userName + " has joined as an observer");
            var notification = new Notification(message);
            connections.add(gameID, authToken, session);
            connections.broadcast(authToken, notification, gameID);

            var loadGame = new LoadGame(gameData);
            connections.sendMessage(gameID, authToken, new Gson().toJson(loadGame));
        } catch (Exception ex) {
            Error error = new Error(ex.getMessage());
            System.out.println(ex.getMessage());
            String message = new Gson().toJson(error);
            connections.sendMessage(gameID, authToken, message);
        }

    }


    public void leaveGame(String action, Session session) {
        try {
            LeaveGame leaveGame = new Gson().fromJson(action, LeaveGame.class);
            String authToken = leaveGame.getAuthToken();
            int gameID = leaveGame.getGameID();
            String userName = getUsername(authToken);

            connections.remove(gameID, authToken, session);
            var message = String.format(userName + " has left the game");
            var notification = new Notification(message);
            connections.broadcast("", notification, gameID);
        } catch (IOException e) {
        }
    }


    public void redraw(String action, Session session) {
        try {
            RedrawBoard redraw = new Gson().fromJson(action, RedrawBoard.class);
            String authToken = redraw.getAuthToken();
            int gameID = redraw.getGameID();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            ChessGame.TeamColor color = getTeamColor(authToken, game);
            var loadGame = new LoadGame(game);
            connections.sendMessage(gameID, authToken, new Gson().toJson(loadGame));
        } catch (Exception e) {
        }
    }


    public void makeMove(String action, Session session) {

            MakeMove makeMove = new Gson().fromJson(action, MakeMove.class);
            int gameID = makeMove.getGameID();
            ChessMove move = makeMove.getMove();
            String authToken = makeMove.getAuthToken();
       try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            ChessGame.TeamColor color = null;

            AuthData auth = authDAO.getAuth(authToken);
            String username = auth.username();

            if (game.game().isGameOver){
                throw new Exception("Game is already over. Cannot make another move");
            }

           if (Objects.equals(game.blackUsername(), username)) {
               color = ChessGame.TeamColor.BLACK;
           }
           else if (Objects.equals(game.whiteUsername(), username)) {
               color = ChessGame.TeamColor.WHITE;
           }
           else {
               Error error = new Error("Error: Not one of the teams");
               String message = new Gson().toJson(error);
               connections.sendMessage(gameID, authToken, message);
               return;
           }

            if (color != game.game().turnColor) {
                Error error = new Error("Error: Not one of the teams");
                String message = new Gson().toJson(error);
                connections.sendMessage(gameID, authToken, message);
                return;
            }
            game.game().makeMove(move);
            gameDAO.updateGame(gameID, game);
            LoadGame newGame = new LoadGame(game);
            connections.broadcast("", newGame, gameID);
            String message = (username + " has made a move: " + move);
            Notification serverMessage = new Notification(message);
            connections.broadcast(authToken, serverMessage, gameID);
        } catch (Exception ex) {
           Error error = new Error(ex.getMessage());
           System.out.println(ex.getMessage());
           String message = new Gson().toJson(error);
           connections.sendMessage(gameID, authToken, message);
       }
    }


    public void resign(String action, Session session) {
        Resign resign = new Gson().fromJson(action, Resign.class);
        int gameID = resign.getGameID();
        String authToken = resign.getAuthToken();

        try {
            AuthData auth = authDAO.getAuth(authToken);
            String username = auth.username();

            GameData game = gameDAO.getGame(gameID);
            if(game.game().isGameOver) {
                throw new Exception("Game is already over");
            }
            else if (!Objects.equals(username, game.blackUsername()) && !Objects.equals(username, game.whiteUsername())){
                throw new Exception("Observers cannot resign game");
            }

            game.game().isGameOver = true;
            gameDAO.updateGame(gameID, game);


            String message = String.format("%s has resigned.\n GAME OVER", username);
            Notification notification = new Notification(message);
            connections.broadcast("", notification, gameID);

        } catch (Exception ex) {
            Error error = new Error(ex.getMessage());
            System.out.println(ex.getMessage());
            String message = new Gson().toJson(error);
            connections.sendMessage(gameID, authToken, message);
        }

    }

    private String getUsername(String authToken) {
        try {
            SQLAuthDAO authDAO = new SQLAuthDAO();
            AuthData authData = authDAO.getAuth(authToken);
            return authData.username();
        } catch (DataAccessException ex) {
            return "";
        }

    }

    private ChessGame.TeamColor getTeamColor(String authToken, GameData gameData){
        String username = getUsername(authToken);
        if (gameData.blackUsername() == username) {
            return ChessGame.TeamColor.BLACK;
        }
        else {
            return ChessGame.TeamColor.WHITE;
        }
    }

    private void authenticateToken(String authToken) throws DataAccessException {
        System.out.print("Authenticating");
        SQLAuthDAO authDAO = new SQLAuthDAO();
        authDAO.getAuth(authToken);
    }

}
