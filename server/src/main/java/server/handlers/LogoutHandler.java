package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{

    public LogoutHandler() throws DataAccessException {
    }

    public Object handle(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            authenticate(authToken);
            authService.delete(authToken);
            res.status(200);
            return "{}";
        } catch (Exception e) {
            return evalException(req, res, e);
        }
    }

}
