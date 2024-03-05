package dataAccess;

import model.UserData;

import java.util.Collection;

public class SQLUserDAO implements UserDAO{
    @Override
    public void clear() {

    }

    @Override
    public void addUser(UserData u) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void updateUser(UserData u) throws DataAccessException {

    }

    @Override
    public void deleteUser(String username) throws DataAccessException {

    }

    @Override
    public Collection<UserData> getUserList() {
        return null;
    }
}
