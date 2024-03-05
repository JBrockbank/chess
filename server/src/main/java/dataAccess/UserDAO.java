package dataAccess;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public interface UserDAO {

    void clear();
    void addUser(UserData u) throws Exception;
    UserData getUser(String username) throws DataAccessException;
    void updateUser(UserData u) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    Collection<UserData> getUserList();





}
