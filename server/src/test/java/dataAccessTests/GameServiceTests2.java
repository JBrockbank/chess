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

    GameDAO gameDAO = new SQLGameDAO();
    UserDAO userDAO = new SQLUserDAO();

    public GameServiceTests2() throws Exception {
    }

    @BeforeEach
    void clear(){
        gameDAO.clear();
        userDAO.clear();
    }

    @Test
    void clearTest() throws DataAccessException {
        assertDoesNotThrow(() -> {
            gameDAO.clear();
        });
        assertTrue(gameDAO.getAllGames().size() == 0);

    }

    @Test
    void createGameTest() throws Exception{
        gameDAO.createGame("game1");
        assertNotNull(gameDAO.getGame(1));
    }

    @Test
    void createGameTestFail() throws Exception{
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame("name");
        });
        assertTrue(exception.getMessage().contains("bad request"));
    }

    @Test
    void joinGameTest() throws Exception {
        gameDAO.createGame("game1");
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        GameData testGame = new GameData(1, "user", "user", "game1", game);
        UserData user = new UserData("user", "pass", "email");
        userDAO.addUser(user);
        GameData gameData1 = gameDAO.getGame(1);
        GameData gameData2 = new GameData(gameData1.gameID(), user.username(), user.username(), gameData1.gameName(), gameData1.game());
        gameDAO.updateGame(1, gameData2);
        assertEquals(gameDAO.getGame(1).blackUsername(), testGame.blackUsername());
        assertEquals(gameDAO.getGame(1).whiteUsername(), testGame.whiteUsername());
    }

    @Test
    void joinGameTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame("game1");
            UserData user = new UserData("user", "pass", "email");
            userDAO.addUser(user);
            GameData gameData1 = gameDAO.getGame(1);
            GameData gameData2 = new GameData(gameData1.gameID(), user.username(), gameData1.blackUsername(), gameData1.gameName(), gameData1.game());
            gameDAO.updateGame(1, gameData2);
            gameDAO.updateGame(1, gameData2);
        });
        assertTrue(exception.getMessage().contains("already taken"));
    }

    @Test
    void listGamesTest() throws Exception {
        gameDAO.createGame("game1");
        Collection<GameData> gameList = gameDAO.getAllGames();
        assertEquals(gameList.size(), 1);
        gameDAO.createGame("game2");
        gameList = gameDAO.getAllGames();
        assertEquals(gameList.size(), 2);
    }

    @Test
    void listGamesTestFail() throws Exception {
        gameDAO.createGame("game1");
        Collection<GameData> gameList = gameDAO.getAllGames();
        UserData user = new UserData("u", "p", "e");
        assertFalse(userDAO.getUser(user.username()) != null);
    }

    @Test
    void getGameTest() throws Exception {
        int id = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(id);
        assertEquals(game.gameName(), "game1");

    }

    @Test
    void getGameTestFail() throws Exception {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            GameData game = gameDAO.getGame(1);
        });
        assertTrue(exception.getMessage().contains("bad request"));
    }

}
