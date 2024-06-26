package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    static HashMap<String, UserData> userList = new HashMap<>();
    @Override
    public void clear() {
        userList.clear();
    }

    @Override
    public void addUser(UserData u) throws DataAccessException {
        if (u.password() == null || u.username() == null) {
            throw new DataAccessException("Error: bad request");
        } else if(userList.get(u.username()) != null){
            throw new DataAccessException("Error: already taken");
        } else {
            userList.put(u.username(), u);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData u = userList.get(username);
        if(u == null){
            throw new DataAccessException("Error: unauthorized");
        }
            return u;
    }






    @Override
    public Collection<UserData> getUserList(){
        return userList.values();
    }

}
