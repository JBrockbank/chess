package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void createGame(String gameName) throws DataAccessException;
    GameData getGame(String gameName) throws DataAccessException;
    void updateGame(String gameName, GameData gameData) throws DataAccessException;
    void deleteGame(String gameName) throws DataAccessException;
    Collection<GameData> getAllGames();


}
