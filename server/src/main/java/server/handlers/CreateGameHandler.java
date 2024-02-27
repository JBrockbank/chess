package server.handlers;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            String gameName = game.gameName();
            Integer gameID = gameService.createGame(gameName);
            res.status(200);
            return gameID;
        } catch (Exception e) {
            return evalException(req, res, e);
        }
    }


}
