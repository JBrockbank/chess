package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    static int gameCount = 0;
    static HashMap<Integer, GameData> gameList = new HashMap<>();
    @Override
    public int createGame(String gameName) throws DataAccessException {
        gameCount++;
        int gameID = gameCount;
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        gameList.put(gameID, gameData);
        return gameID;
    }



    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if(gameList.get(gameID) != null){
            return gameList.get(gameID);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        if(gameList.get(gameID) != null){
            gameList.replace(gameID, gameData);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }


    public void deleteGame(int gameID) throws DataAccessException {
        if(gameList.get(gameID) != null){
            gameList.remove(gameID);
        }
        else {
            throw new DataAccessException("Error: bad request");
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
