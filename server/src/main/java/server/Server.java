package server;

import dataAccess.DataAccessException;
import server.handlers.*;
import spark.*;


public class Server {



    public int run(int desiredPort) {
        Spark.port(desiredPort);

//        Spark.staticFiles.location("main/resources");
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        createRoutes();




        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes() {
        Spark.delete("/db", (req, res) -> new ClearAllHandler().handle(req, res));
        Spark.post("/user", (req, res) -> new RegisterUserHandler().handle(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().handle(req, res));

    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}