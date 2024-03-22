package server;

import dataAccess.DataAccessException;
import server.handlers.*;
import spark.Spark;


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
        System.out.println("Creating Routes");
        Spark.delete("/db", (req, res) -> new ClearAllHandler().handle(req, res));
        Spark.post("/user", (req, res) -> new RegisterUserHandler().handle(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().handle(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().handle(req, res));
        Spark.get("/game", (req, res) -> new ListGamesHandler().handle(req, res));
        Spark.post("/game", (req, res) -> new CreateGameHandler().handle(req, res));
        Spark.put("/game", (req, res) -> new JoinGameHandler().handle(req, res));

    }





    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}