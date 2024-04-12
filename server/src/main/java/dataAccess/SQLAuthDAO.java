package dataAccess;

import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() {
        try {
            System.out.print("Here SQL");
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.print(e.getMessage());
            throw new RuntimeException(e);
        }
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
            String sql = "INSERT into authData (authToken, username) VALUES (?, ?)";
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
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * from authData WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException("Error: unauthorized");
            }
            String username = rs.getString("username");
            return new AuthData(token, username);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        if (getAuth(token) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            String update = "DELETE FROM authData WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, token);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String update = "TRUNCATE authData";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.executeUpdate();
        }
        catch (Exception e) {

        }
    }
}
