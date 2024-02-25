package dataAccess;
import model.AuthData;

public interface AuthDAO {
    void createAuth(String username) throws DataAccessException;
    AuthData getAuth(AuthData token) throws DataAccessException;
    void deleteAuth(AuthData token) throws DataAccessException;
    void clear();

}
