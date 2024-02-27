package service;

import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;

public class GameService {

    private static GameDAO gameDAO;

    public GameService(){
        gameDAO = new MemoryGameDAO();
    }

    public void clear(){
        gameDAO.clear();

    }

}
