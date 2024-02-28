package dataAccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{

    static HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authMap.put(authToken, authData);
        return authData;
    }
    @Override
    public AuthData getAuth(String token) throws DataAccessException{
        if(authMap.get(token) != null) {
            return authMap.get(token);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    @Override
    public void deleteAuth(String token) throws DataAccessException{
        if(authMap.get(token) != null) {
            authMap.remove(token);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
