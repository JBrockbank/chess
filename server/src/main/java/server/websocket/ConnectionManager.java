
package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
//    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
public final HashMap<Integer, HashMap<String, Session>> connections = new HashMap<>();


    public void add(int gameID, String authToken, Session session) {
        HashMap<String, Session> sessionHashMap = connections.get(gameID);
        if (sessionHashMap == null) {
            sessionHashMap = new HashMap<String, Session>();
        }
        sessionHashMap.put(authToken, session);
        connections.put(gameID, sessionHashMap);
    }

    public void remove(int ID, String authToken, Session session) {
        Integer gameID = Integer.valueOf(ID);
        HashMap<String, Session> sessionHashMap = connections.get(gameID);
        sessionHashMap.remove(authToken);

    }

//    public void sendErrorMessage(int gameID, String authToken, Error message) throws IOException {
//        System.out.print("Sending Error Message");
//        HashMap<String, Session> sessionHashMap = connections.get(gameID);
//        Session session = sessionHashMap.get(authToken);
//        System.out.print("\nMessage: " + message.getMessage());
//        session.getRemote().sendString(new Gson().toJson(message));
//    }

    public void sendMessage(int gameID, String authToken, String message) {
        try {
            HashMap<String, Session> sessionHashMap = connections.get(gameID);
            Session session = sessionHashMap.get(authToken);
            session.getRemote().sendString(message);
        } catch (IOException ex) {
            System.out.print("IO Exception");
        }

    }
    public void broadcast(String excludeAuthToken, ServerMessage message, int gameID) throws IOException {
        var removeList = new ArrayList<String>();
        HashMap<String, Session> userAuthList = connections.get(gameID);
        for (String userAuth : userAuthList.keySet()) {
            if (userAuthList.get(userAuth).isOpen()) {
                if (!Objects.equals(userAuth, excludeAuthToken)) {
                    userAuthList.get(userAuth).getRemote().sendString(new Gson().toJson(message));
                }
            } else {
                removeList.add(userAuth);
            }
        }
        for (String closed : removeList) {
            userAuthList.remove(closed);
        }

    }
}
