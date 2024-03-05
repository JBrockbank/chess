package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import server.responses.RegisterUserResponse;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler{

    public LoginHandler() throws DataAccessException {
    }

    public Object handle(Request req, Response res) throws DataAccessException{
        try {
            UserData userData = new Gson().fromJson(req.body(), UserData.class);
            if (userService.verifyUser(userData)){
                AuthData authData = authService.newToken(userData.username());
                RegisterUserResponse response = new RegisterUserResponse(authData.username(), authData.authToken());
                return response.toJSon();
            }
            else {
                throw new DataAccessException("Error: unauthorized");
            }
        }
        catch (DataAccessException e) {
            return evalException(req, res, e);
        }
    }
}
