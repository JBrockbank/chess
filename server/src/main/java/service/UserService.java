package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

public class UserService {

    public UserDAO userDAO;

    public  UserService() {
        try {
            userDAO = new SQLUserDAO();
        }
        catch (Exception e) {

        }
    }

    public Object addUser(UserData user) throws DataAccessException{
        try {
            userDAO.addUser(user);
            return null;
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean verifyUser(UserData user) throws DataAccessException{
        String username = user.username();
        String password = user.password();
        UserData existingUser = userDAO.getUser(username);
        if (existingUser.password().equals(password)){
            return true;
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }

    }


    public void clear(){
        userDAO.clear();
    }

}
