package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import spark.Request;
import spark.Response;

import java.util.Map;

public class JoinGameHandler extends Handler{

    public JoinGameHandler() throws DataAccessException {
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            AuthData authData = authService.getAuthData(authToken);
            String username = authData.username();
            Gson gson = new Gson();
            Map<String, Object> requestBody = gson.fromJson(req.body(), Map.class);
            String playerColor = (String) requestBody.get("playerColor");
            double dgameID = (double) requestBody.get("gameID");
            int gameID = (int) dgameID;
            gameService.joinGame(gameID, playerColor, username);
            res.status(200);
            return "{}";
        } catch (Exception e) {
            return evalException(req, res, e);

        }
    }
}
