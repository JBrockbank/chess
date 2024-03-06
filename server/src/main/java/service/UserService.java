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
            String password = user.password();
            if (password == null){
                throw new DataAccessException("Error: bad request");
            }
            String hash = hashpassword(password);
            UserData newUser = new UserData(user.username(), hash, user.email());
            userDAO.addUser(newUser);
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
        else if (new BCryptPasswordEncoder().matches(password, existingUser.password())){
            return true;
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    public String hashpassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        return hashedPassword;
    }




    public void clear(){
        userDAO.clear();
    }

}
