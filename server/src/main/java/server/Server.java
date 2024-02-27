package server;

import server.handlers.*;
import spark.*;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

//        Spark.staticFiles.location("main/resources");
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        try {
            createRoutes();
        } catch (Exception e) {

        }



        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes() throws Exception{
        Spark.get("/hello", (req, res) -> "Hello");
        Spark.delete("/db", (req, res) -> new ClearAllHandler().handle(req, res));

    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}