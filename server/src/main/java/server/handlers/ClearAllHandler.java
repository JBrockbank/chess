package server.handlers;

import spark.Request;
import spark.Response;
import server.services.*;

public class ClearAllHandler extends Handler{

    @Override
    public Object handle(Request req, Response res) throws Exception {

        ClearAllService service = new ClearAllService();

    }
}
