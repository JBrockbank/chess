package dataAccess;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public interface UserDAO {

    void clear();
    void addUser(UserData u) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void updateUser(UserData u) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    Collection<UserData> getUserList();





}
