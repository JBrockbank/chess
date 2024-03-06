package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static service.GameService.gameDAO;

public class GameServiceTests2 {

    GameService gameService = new GameService();
    UserService userService = new UserService();

    @BeforeEach
    void clear(){
        gameService.clear();
        userService.clear();
    }

    @Test
    void clearTest() throws DataAccessException {
        assertDoesNotThrow(() -> {
            gameService.clear();
        });
        assertTrue(gameService.gameDAO.getAllGames().size() == 0);

    }

    @Test
    void createGameTest() throws Exception{
        gameService.createGame("game1");
        assertNotNull(gameDAO.getGame(1));
    }

    @Test
    void createGameTestFail() throws Exception{
        gameService.createGame("game1");
        UserData user = new UserData("user", "pass", "email");
        userService.verifyUser(user);
        assertFalse(userService.verifyUser(user));
    }

    @Test
    void joinGameTest() throws Exception {
        gameService.createGame("game1");
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        GameData testGame = new GameData(1, "user", "user", "game1", game);
        UserData user = new UserData("user", "pass", "email");
        userService.addUser(user);
        gameService.joinGame(1, "WHITE", user.username());
        gameService.joinGame(1, "BLACK", user.username());
        gameService.joinGame(1, "", user.username());
        assertEquals(gameService.getGame(1).blackUsername(), testGame.blackUsername());
        assertEquals(gameService.getGame(1).whiteUsername(), testGame.whiteUsername());
    }

    @Test
    void joinGameTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameService.createGame("game1");
            UserData user = new UserData("user", "pass", "email");
            userService.addUser(user);
            gameService.joinGame(1, "WHITE", user.username());
            gameService.joinGame(1, "WHITE", user.username());
        });
        assertTrue(exception.getMessage().contains("already taken"));
    }

    @Test
    void listGamesTest() throws Exception {
        gameService.createGame("game1");
        Collection<GameData> gameList = gameService.ListGames();
        assertEquals(gameList.size(), 1);
        gameService.createGame("game2");
        gameList = gameService.ListGames();
        assertEquals(gameList.size(), 2);
    }

    @Test
    void listGamesTestFail() throws Exception {
        gameService.createGame("game1");
        Collection<GameData> gameList = gameService.ListGames();
        UserData user = new UserData("u", "p", "e");
        assertFalse(userService.verifyUser(user));
    }
}
