package server.handlers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{

    public Object handle(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            authService.delete(authToken);
            res.status(200);
            return new Gson().toJson(res.status());
        } catch (Exception e) {
            if(e.getMessage().equals("Error: unauthorized")){
                res.status(401);
                return new Gson().toJson(e.getMessage());
            }
            res.status(500);
            return new Gson().toJson(e.getMessage());
        }
    }

}
