package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import model.UserData;

public class AuthService {

    public static AuthDAO authDAO;

    public AuthService() throws DataAccessException {
        try {
            authDAO = new SQLAuthDAO();
        } catch (Exception e) {

        }
    }

    public AuthData newToken(String username) throws DataAccessException{
        AuthData authData = authDAO.createAuth(username);
        return authData;
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
