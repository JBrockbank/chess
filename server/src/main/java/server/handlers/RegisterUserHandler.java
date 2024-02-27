package server.handlers;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import spark.Request;
import spark.Response;

public class RegisterUserHandler extends Handler{

    public Object handle(Request req, Response res){
        try {
            var newUser = new Gson().fromJson(req.body(), UserData.class);
            userService.addUser(newUser);
            String username = newUser.username();
            AuthData authData = authService.newToken(username);
            var data = new Gson().toJson(authData);
            res.status(200);
            return data;
        } catch (Exception e){
            return evalException(req, res, e);
        }

    }
}
