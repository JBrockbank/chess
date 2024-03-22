package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;
import responses.GameResponse;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler{

    public CreateGameHandler() throws DataAccessException {
    }

    public Object handle(Request req, Response res) {
        try {
            System.out.println("GameHandler");
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            String gameName = game.gameName();
            Integer gameID = gameService.createGame(gameName);
            GameResponse response = new GameResponse(gameID);
            System.out.println("New Game gameID: " + gameID);
            res.status(200);
            return response.toJSon();
        } catch (Exception e) {
            System.out.println("Exception Thrown");
            System.out.println(e.getMessage());
            return evalException(req, res, e);
        }
    }


}
