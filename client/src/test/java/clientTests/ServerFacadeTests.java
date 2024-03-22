package clientTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import responses.GameListResponse;
import server.Server;
import client.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);

    }

    @BeforeEach
    public void clearDB() throws Exception {
      facade.clearDB();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }





    @Test
    void register() throws Exception {
        var authToken = facade.register("player1", "password", "p1@email.com");
        assertTrue(authToken.length() > 10);
    }

    @Test
    void registerNegative() {
        assertThrows(Exception.class, () -> {
            var authToken = facade.register("player1", null, "p1@email.com");
            assertTrue(authToken.length() > 10);
        });
    }


    @Test
    void signIn() throws Exception {
        facade.register("user1", "pass1", "email1");
        var authToken = facade.signIn("user1", "pass1");
        assertTrue(authToken.length() > 10);
    }


    @Test
    void signInNegative() throws Exception {
        Exception e = assertThrows(Exception.class, () -> {
            var authToken = facade.signIn("user1", "pass1");
            assertFalse(authToken.length() > 10);
        });
    }

    @Test
    void createGames() throws Exception {
        facade.register("user1", "pass1", "email1");
        facade.createGame("Game1");
        Collection<GameData> gameList = facade.listGames();
        assertTrue(gameList.size() > 0);
    }

    @Test
    void createGamesNegative(){
        assertThrows(Exception.class, () -> {
            facade.createGame("Game1");
        });
    }

    @Test
    void listGames() throws Exception {
        facade.register("user1", "pass1", "email1");
        facade.createGame("Game1");
        Collection<GameData> gameList = facade.listGames();
        assertTrue(gameList.size() == 1);
    }


    @Test
    void listGamesNegative() {
        assertThrows(Exception.class, () -> {
            Collection<GameData> gameList = facade.listGames();
        });
    }

    @Test
    void joinGame() throws Exception {
        facade.register("user1", "pass1", "email1");
        facade.createGame("Game1");
        GameData game = facade.joinGame(1, "WHITE");
        assertNotNull(game);
    }

    @Test
    void joinGameNegative() {
        assertThrows(Exception.class, () -> {
            facade.register("user1", "pass1", "email1");
            facade.joinGame(1, "WHITE");
        });
    }

    @Test
    void observeGame() throws Exception {
        facade.register("user1", "pass1", "email1");
        facade.createGame("Game1");
        GameData game = facade.observeGame(1);
        assertNotNull(game);
    }


    @Test
    void observeGameNegative() {
        assertThrows(Exception.class, () -> {
            facade.register("user1", "pass1", "email1");
            facade.joinGame(1, "WHITE");
        });
    }


    @Test
    void logout() {
        assertThrows(Exception.class, () -> {
            facade.register("user1", "pass1", "email1");
            facade.logout();
            facade.createGame("Game1");
        });
    }

    @Test
    void logoutNegative() {
        assertThrows(Exception.class, () -> {
            facade.logout();
        });
    }

}
