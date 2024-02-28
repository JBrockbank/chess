package server.handlers;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import server.responses.RegisterUserResponse;
import spark.Request;
import spark.Response;

public class RegisterUserHandler extends Handler{

    public Object handle(Request req, Response res){
        try {
            var newUser = new Gson().fromJson(req.body(), UserData.class);
            userService.addUser(newUser);
            String username = newUser.username();
            AuthData authData = authService.newToken(username);
            res.status(200);
            var response = new RegisterUserResponse(username, authData.authToken());
            return response.toJSon();
        } catch (Exception e){
            return evalException(req, res, e);
        }
    }
}
