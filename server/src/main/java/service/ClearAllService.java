package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearAllService {

    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;

    public ClearAllService(UserDAO user, AuthDAO auth, GameDAO game){
        userDAO = user;
        authDAO = auth;
        gameDAO = game;
    }
    public void ClearAll(){

        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();

    }


}
