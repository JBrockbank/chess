package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        if (existingUser == null) {
            return false;
        }
        else if (existingUser.password().equals(password)){
            return true;
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    public UserData hashpassword(UserData u) {
        String password = u.password();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return new UserData(u.username(), hashedPassword, u.email());
    }


    public void clear(){
        userDAO.clear();
    }

}
