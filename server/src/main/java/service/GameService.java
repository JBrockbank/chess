package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.SQLGameDAO;
import model.GameData;

import java.util.Collection;

public class GameService {

    public static GameDAO gameDAO;

    public GameService(){
        try {
            gameDAO = new SQLGameDAO();
        }
        catch (Exception e) {

        }
    }

    public Collection<GameData> ListGames(){
        return gameDAO.getAllGames();
    }

    public int createGame(String gameName)throws DataAccessException{
        return gameDAO.createGame(gameName);
    }

    public void joinGame(int gameID, String playerColor, String username) throws DataAccessException{
        GameData gameData = gameDAO.getGame(gameID);
        if(playerColor.equals("empty") || playerColor.isEmpty()){

        }
        else if (playerColor.equals("WHITE")){
            if (gameData.whiteUsername() == null){
                GameData newGameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDAO.updateGame(gameID, newGameData);
            }
            else {
                throw new DataAccessException("Error: already taken");
            }
        }
        else if (playerColor.equals("BLACK")){
            if (gameData.blackUsername() == null){
                GameData newGameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                gameDAO.updateGame(gameID, newGameData);
            }
            else {
                throw new DataAccessException("Error: already taken");
            }
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public void clear(){
        gameDAO.clear();

    }

}
