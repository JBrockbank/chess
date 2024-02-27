package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;
import service.*;


public class Handler {

    GameService gameService = new GameService();
    UserService userService = new UserService();
    AuthService authService = new AuthService();
    Gson serializer = new Gson();

    public Object handle(Request req, Response res) throws Exception{
        throw new Exception("Not Implemented");
    }

    public void authenticate(String authToken) throws Exception{
        AuthData authData = authService.getAuthData(authToken);
        if (authData == null){
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
