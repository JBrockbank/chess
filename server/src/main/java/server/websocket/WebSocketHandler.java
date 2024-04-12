package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

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

        }
    }


    private void joinGame(String action, Session session) throws ResponseException {
        System.out.print("Server.WebSocketHandler.joinGame called\n");
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(action, JoinPlayer.class);
            String userName = joinPlayer.getAuthString();
            GameData game = joinPlayer.getGame();
            ChessGame.TeamColor color = joinPlayer.getColor();
            var message = String.format(userName + " has joined " + color.toString());
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            //Pass in game ID to seperate the difference connection maps
            connections.add(userName, session);
            connections.broadcast(userName, notification);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(userName, loadGame);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            throw new ResponseException(500, e.getMessage());
        }
    }


    private void joinObserver(String action, Session session) throws ResponseException {
        try {
            JoinObserver joinObserver = new Gson().fromJson(action, JoinObserver.class);
            String userName = joinObserver.getAuthString();
            GameData game = joinObserver.getGame();
            var message = String.format(userName + " has joined as an observer");
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            //Pass in game ID to seperate the difference connection maps
            connections.add(userName, session);
            connections.broadcast(userName, notification);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(userName, loadGame);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }

    }


    public void leaveGame(String action, Session session) throws ResponseException {
        try {
            LeaveGame leaveGame = new Gson().fromJson(action, LeaveGame.class);
            String userName = leaveGame.getAuthToken();
            connections.remove(userName);
            var message = String.format(userName + " has left the game");
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast("", notification);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void redraw(String action, Session session) throws ResponseException {
        try {
            RedrawBoard redraw = new Gson().fromJson(action, RedrawBoard.class);
            String username = redraw.getAuthToken();
            int gameID = redraw.getGameID();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            var loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            loadGame.game = game;
            connections.sendMessage(username, loadGame);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void makeMove(String action, Session session) throws ResponseException {
        try {
            MakeMove makeMove = new Gson().fromJson(action, MakeMove.class);
            int gameID = makeMove.getGameID();
            ChessPosition startPos = makeMove.getStartPos();
            ChessPosition endPos = makeMove.getEndPos();
            ChessGame.TeamColor color = makeMove.getPlayerColor();

            String username = makeMove.getAuthToken();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);

            if (color != game.game().turnColor) {
                throw new ResponseException(500, "Error: Not your turn");
            }

            ChessMove move = new ChessMove(startPos, endPos, null);
            game.game().makeMove(move);

            gameDAO.updateGame(gameID, game);

            LoadGame newGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME);
            newGame.game = game;
            connections.broadcast("", newGame);
            String message = (username + " has made a move: " + startPos.toString() + " to " + endPos.toString());
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(username, serverMessage);
        } catch (InvalidMoveException ex) {
            System.out.print("Error - InvalidMoveException: " + ex.getMessage());
        } catch (DataAccessException ex) {
            System.out.print("Error - DataAccessException: " + ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
