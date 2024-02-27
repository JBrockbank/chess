package server.handlers;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

    public Object handle(Request req, Response res) {
        try {
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            String gameName = game.gameName();
            Integer gameID = gameService.createGame(gameName);
            res.status(200);
            return gameID;
        } catch (Exception e) {
            if (e.getMessage().equals("Error: bad request")){
                res.status(400);
                return new Gson().toJson(e.getMessage());
            }
            else if (e.getMessage().equals("Error: unauthorized")) {
                res.status(401);
                return new Gson().toJson(e.getMessage());
            }
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }

}
