package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import model.UserData;
import server.responses.GameResponse;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

    public CreateGameHandler() throws DataAccessException {
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            String gameName = game.gameName();
            Integer gameID = gameService.createGame(gameName);
            res.status(200);
            GameResponse response = new GameResponse(gameID);
            return response.toJSon();
        } catch (Exception e) {
            return evalException(req, res, e);
        }
    }


}
