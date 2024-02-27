package server.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import server.responses.BaseResponse;
import service.ClearAllService;
import spark.Request;
import spark.Response;

public class ClearAllHandler extends Handler{

    @Override
    public Object handle(Request req, Response res) throws Exception {

        ClearAllService service = new ClearAllService(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        service.ClearAll();
        res.status(200);
        var serializer = new Gson();
        return serializer.toJson(res);
    }
}
