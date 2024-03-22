package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.UserData;
import server.responses.ErrorResponse;
import spark.Request;
import spark.Response;
import service.*;


public class Handler {

    GameService gameService = new GameService();
    UserService userService = new UserService();
    AuthService authService = new AuthService();
    Gson serializer = new Gson();

    public Handler() throws DataAccessException {
    }

    public Object handle(Request req, Response res) throws Exception{
        throw new Exception("Not Implemented");
    }

    public void authenticate(String authToken) throws Exception{
        AuthData authData = authService.getAuthData(authToken);
        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public Object evalException(Request req, Response res, Exception e){
        ErrorResponse response = new ErrorResponse(e.getMessage());
        if (e.getMessage().equals("Error: bad request")){
            res.status(400);
        }
        else if (e.getMessage().equals("Error: unauthorized")) {
            res.status(401);
        } else if (e.getMessage().equals("Error: already taken")) {
            res.status(403);
        }
        else {
            res.status(500);
        }
        return response.toJSon();
    }


}
