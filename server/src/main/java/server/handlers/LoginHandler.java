package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler{

    public Object handle(Request req, Response res) throws DataAccessException{
        try {
            UserData userData = new Gson().fromJson(req.body(), UserData.class);
            if (userService.verifyUser(userData)){
                AuthData authData = authService.newToken(userData.username());
                return new Gson().toJson(authData);
            }
        }
        catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
            res.status(401);
            return new Gson().toJson(e.getMessage());
        }
        res.status(500);
        return new Gson().toJson((e.getMessage()));
        }
        return null;
    }


}
