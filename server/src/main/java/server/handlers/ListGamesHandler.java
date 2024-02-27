package server.handlers;

import com.google.gson.Gson;
import model.GameData;
import spark.Request;
import spark.Response;
import java.util.Collection;

public class ListGamesHandler extends Handler{

    public Object handle(Request req, Response res){
        try {
            Collection<GameData> games = gameService.ListGames();
            res.status(200);
            return new Gson().toJson(games);
        } catch (Exception e) {
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }

}
