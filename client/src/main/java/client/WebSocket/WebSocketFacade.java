package client.WebSocket;

import chess.ChessGame;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import ui.DrawBoard;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    private ChessGame.TeamColor playerColor;
    private DrawBoard draw = new DrawBoard();
    private List<GameData> gameDataList = new ArrayList<>();


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = new NotificationHandler() {
                @Override
                public void notify(ServerMessage serverMessage, String message) {
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> notification(message);
                        case ERROR -> error(message);
                        case LOAD_GAME -> loadGame(message);
                    }
                }
            };

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification, message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {

        }
    }

//    @OnWebSocketError
//    public void onWebSocketError(Throwable cause){
//        System.err.println("WebSocket error occurred:");
//        System.err.println(cause.getMessage());
//        cause.printStackTrace();
//    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void error(String message) {
        Error error  = new Gson().fromJson(message, Error.class);
        System.out.print(error.getMessage());
    }

    public void notification(String message) {
        Notification notification  = new Gson().fromJson(message, Notification.class);
        System.out.print(notification.getMessage());
    }


    public void joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws Exception {
        try {
            System.out.print("\nClient.WebSocketFacade.joinGame called");
            playerColor = color;
            var action = new JoinPlayer(authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
            System.out.print("Here");
        }
    }

    public void joinObserver(String authToken, int gameID) throws Exception {
        try {
            var action = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void loadGame(String message) {
        System.out.print("WSF: Load Game Called");
        LoadGame loadGame  = new Gson().fromJson(message, LoadGame.class);
        GameData game = loadGame.getGame();
        String bUsername = game.blackUsername();
        String wUsername = game.whiteUsername();

        draw.displayGame(game, playerColor, null);
    }


    public void leave(String authToken, int gameID) {
        try {
            var action = new LeaveGame(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void redrawBoard(String authToken, int gameID) throws Exception {
        try {
            var action = new RedrawBoard(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove(String authToken, ChessMove move, int gameID) throws IOException {
        var action = new MakeMove(authToken, move, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    }

    public void resign(String authToken, int gameID) throws IOException {
        var action = new Resign(authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    }



}