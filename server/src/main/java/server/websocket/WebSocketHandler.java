package server.websocket;

import chess.ChessPiece;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.print("Server.WebSocketHandler.onMessage called");
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinGame((JoinPlayer) action, session);
        }
    }

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new ServerMessage(ServerMessage.ServerMessageType.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }


    private void joinGame(JoinPlayer action, Session session) throws ResponseException {
        System.out.print("Server.WebSocketHandler.joinGame called\n");
        try {
            String userName = action.getAuthString();
            GameData game = action.getGame();
            var message = String.format(userName + " has joined the game");
            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
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

//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new ServerMessage(ServerMessage.ServerMessageType.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}