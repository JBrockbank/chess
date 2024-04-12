package client;

import chess.ChessGame;
import com.google.gson.Gson;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.glassfish.grizzly.http.server.Request;
import responses.*;

import java.io.*;
import java.net.*;
import java.util.Collection;


public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(int urlPort) {
        String url = "http://localhost:";
        url += String.valueOf(urlPort);
        serverUrl = url;
    }


    public String signIn(String username, String password) throws Exception{
        var path = "/session";
        UserData user = new UserData(username, password, null);
        RegisterUserResponse res = this.makeRequest("POST", path, user, RegisterUserResponse.class);
        authToken = res.authToken;
        return authToken;
    }

    public String register(String username, String password, String email) throws Exception {
        var path = "/user";
        UserData user = new UserData(username, password, email);
        RegisterUserResponse res = this.makeRequest("POST", path, user, RegisterUserResponse.class);
        authToken = res.authToken;
        return authToken;
    }

    public void createGame(String name) throws Exception {
        var path = "/game";
        GameData game = new GameData(0, null, null, name, null);
        GameResponse res = this.makeRequest("POST", path, game, GameResponse.class);
    }

    public GameData joinGame(int gameID, String color) throws Exception {
        var path = "/game";
        var req = new JoinGameData(color, gameID);
        return this.makeRequest("PUT", path, req, GameData.class);
    }

    public void logout() throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, BaseResponse.class);
        authToken = null;
    }

    public Collection<GameData> listGames() throws Exception {
        var path = "/game";
        GameListResponse res = this.makeRequest("GET", path, null, GameListResponse.class);
        return res.games;
    }

    public GameData observeGame(int gameID) throws Exception {
        var path = "/game";
        var req = new JoinGameData("empty", gameID);
        return this.makeRequest("PUT", path, req, GameData.class);
    }

    public void clearDB() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }




    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }
            if (request != null){
                http.setDoOutput(true);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            var res = readBody(http, responseClass);
            return res;
        } catch (Exception ex) {

            throw new Exception(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("Error: Not successful");
        }
    }


    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);

                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }




}



