package server.handlers;

import com.google.gson.Gson;
import model.GameData;
import server.responses.GameListResponse;
import spark.Request;
import spark.Response;
import java.util.Collection;

public class ListGamesHandler extends Handler{

    public Object handle(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            Collection<GameData> games = gameService.ListGames();
            res.status(200);
            GameListResponse response = new GameListResponse(games);
            String output = response.toJSon();
            return output;
        } catch (Exception e) {
            return evalException(req, res, e);
        }
    }

}
