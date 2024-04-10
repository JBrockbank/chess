package client.WebSocket;

import chess.ChessGame;

import com.google.gson.Gson;
import model.GameData;
import webSocketMessages.ResponseException;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = new NotificationHandler() {
                @Override
                public void notify(ServerMessage message) {
                    switch (message.getServerMessageType()) {
                        case NOTIFICATION -> System.out.println("Notification: " + message.getMessage());
                        case ERROR -> System.out.println("Error");
                        case LOAD_GAME -> loadGame((LoadGame) message);
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
                    System.out.print("Client.WebSocketFacade.onMessage called. \nMessageType: " + notification.getServerMessageType().toString() + "\nMessage: " + notification.getMessage());
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.print("Client.WebSocketFacade.onOpen called");

    }

    public void joinGame(String username, int gameID, ChessGame.TeamColor color, GameData gameData) throws Exception {
        try {
            System.out.print("Client.WebSocketFacade.joinGame called");
            var action = new JoinPlayer(username, gameID, color, gameData);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void loadGame(LoadGame gameMessage) {
        
    }



//    public void leavePetShop(String visitorName) throws ResponseException {
//        try {
//            var action = new UserGameCommand(UserGameCommand.CommandType.EXIT, visitorName);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//            this.session.close();
//        } catch (IOException ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

}