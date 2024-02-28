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
            System.out.println("ListGames");

            String authToken = req.headers("Authorization");
            authenticate(authToken);
            Collection<GameData> games = gameService.ListGames();
            res.status(200);
//            if (games == null){
//                return "{}";
//            }
            System.out.println(games);
            GameListResponse response = new GameListResponse(games);
            System.out.println(response.games);
            String output = response.toJSon();
            System.out.println(output);
            return output;
        } catch (Exception e) {
            return evalException(req, res, e);
        }
    }

}
