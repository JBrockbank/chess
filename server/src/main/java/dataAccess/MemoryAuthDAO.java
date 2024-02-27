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
    public AuthData getAuth(AuthData token) throws DataAccessException{
        if(authMap.get(token) != null) {
            return authMap.get(token);
        }
        else {
            throw new DataAccessException("Error: Unauthorized");
        }
    }
    @Override
    public void deleteAuth(AuthData token) throws DataAccessException{
        if(authMap.get(token) != null) {
            authMap.remove(token);
        }
        else {
            throw new DataAccessException("Error: Auth Token does not exist");
        }
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
