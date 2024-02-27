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
            var serializer = new Gson();
            return serializer.toJson(null);
        } catch (Exception e){
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }
}
