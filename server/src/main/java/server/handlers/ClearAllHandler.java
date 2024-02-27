package server.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class ClearAllHandler extends Handler{

    @Override
    public Object handle(Request req, Response res) {
        try {
            gameService.clear();
            userService.clear();
            authService.clear();
            res.status(200);
            return new Gson().toJson(res.status());
        } catch (Exception e){
            return evalException(req, res, e);
        }
    }
}
