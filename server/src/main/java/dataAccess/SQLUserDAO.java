package dataAccess;

import model.UserData;

import java.security.spec.ECField;
import java.sql.*;
import java.util.Collection;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws Exception {
        configureDatabase();
    }


    @Override
    public void clear() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String update = "TRUNCATE userData";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.executeUpdate();
        }
        catch (Exception e) {

        }
    }

    @Override
    public void addUser(UserData u) throws SQLException, DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            if (u.password() == null || u.username() == null) {
                throw new DataAccessException("Error: bad request");
            } else if(getUser(u.username()) != null){
                throw new DataAccessException("Error: already taken");
            } else {
                String update = "INSERT into userData (username, password, email) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(update);
                stmt.setString(1, u.username());
                stmt.setString(2, u.password());
                stmt.setString(3, u.email());
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * from userData WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()){
                return null;
            }
            String password = rs.getString("password");
            String email = rs.getString("email");
            return new UserData(username, password, email);
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public Collection<UserData> getUserList() {
        return null;
    }


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

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `userData` (
              `username` varchar(255) UNIQUE NOT NULL,
              `password` varchar(255) NOT NULL,
              `email` varchar(255)
            )
            """
    };



}
