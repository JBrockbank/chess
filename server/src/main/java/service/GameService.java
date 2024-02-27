package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {

    private static GameDAO gameDAO;

    public GameService(){
        gameDAO = new MemoryGameDAO();
    }

    public Collection<GameData> ListGames(){
        return gameDAO.getAllGames();
    }

    public int createGame(String gameName)throws DataAccessException{
        return gameDAO.createGame(gameName);
    }

    public void clear(){
        gameDAO.clear();

    }

}
