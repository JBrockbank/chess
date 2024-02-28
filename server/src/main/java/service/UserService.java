package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.UserData;

public class UserService {

    public UserDAO userDAO;

    public  UserService(){
        userDAO = new MemoryUserDAO();
    }

    public Object addUser(UserData user) throws DataAccessException{
        userDAO.addUser(user);
        return null;
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
