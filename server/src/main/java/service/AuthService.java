package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

public class AuthService {

    private static AuthDAO authDAO;

    public AuthService(){
        authDAO = new MemoryAuthDAO();
    }

    public AuthData newToken(String username){
        try {
            AuthData authData = authDAO.createAuth(username);
            return authData;
        } catch (DataAccessException e) {

        }
        return null;
    }

    public AuthData getAuthData(String authToken) throws DataAccessException{
        return authDAO.getAuth(authToken);
    }

    public void delete(String authToken) throws DataAccessException{
        authDAO.deleteAuth(authToken);
    }



    public void clear(){
        authDAO.clear();
    }

}
