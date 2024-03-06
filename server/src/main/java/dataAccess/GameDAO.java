package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO {

    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(int gameID, GameData gameData) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    Collection<GameData> getAllGames() throws DataAccessException;
    void clear();


}
