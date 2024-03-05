package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws Exception{
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `authData` (
              `authToken` varchar(255) PRIMARY KEY NOT NULL,
              `username` varchar(255) NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }



    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO authData (authToken, authData) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, username);
            stmt.setString(1, authToken);
            stmt.setString(2, username);
            stmt.executeUpdate();
            return authData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
