package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
        } catch (Exception e) {

        }

        return null;
    }
}
