package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    int gameCount = 0;
    static HashMap<String, GameData> gameList = new HashMap<>();
    @Override
    public void createGame(String gameName) throws DataAccessException {
        gameCount++;
        int gameID = gameCount;
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameList.put(gameName, gameData);
    }

    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        if(gameList.get(gameName) != null){
            return gameList.get(gameName);
        }
        else {
            throw new DataAccessException("Error: Game not found");
        }
    }

    @Override
    public void updateGame(String gameName, GameData gameData) throws DataAccessException {
        if(gameList.get(gameName) != null){
            gameList.replace(gameName, gameData);
        }
        else {
            throw new DataAccessException("Error: Game not found");
        }
    }

    @Override
    public void deleteGame(String gameName) throws DataAccessException {
        if(gameList.get(gameName) != null){
            gameList.remove(gameName);
        }
        else {
            throw new DataAccessException("Error: Game not found");
        }
    }

    @Override
    public Collection<GameData> getAllGames() {
        return gameList.values();
    }

    @Override
    public void clear(){
        gameList.clear();
    }
}
