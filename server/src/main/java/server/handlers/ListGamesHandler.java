package server.handlers;

import dataAccess.DataAccessException;
import model.GameData;
import responses.GameListResponse;
import spark.Request;
import spark.Response;
import java.util.Collection;

public class ListGamesHandler extends Handler{

    public ListGamesHandler() throws DataAccessException {
    }

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
