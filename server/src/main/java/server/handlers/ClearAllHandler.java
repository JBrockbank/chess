package server.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import server.responses.BaseResponse;
import spark.Request;
import spark.Response;

public class ClearAllHandler extends Handler{

    public ClearAllHandler() throws DataAccessException {
    }

    @Override
    public Object handle(Request req, Response res) {
        try {
            gameService.clear();
            userService.clear();
            authService.clear();
            res.status(200);
            return "{}";
        } catch (Exception e){
            return evalException(req, res, e);
        }
    }
}
