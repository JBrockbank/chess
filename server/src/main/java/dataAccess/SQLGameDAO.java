package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GameData;

import java.security.spec.ECField;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO{


    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        Gson serializer = new Gson();
        try (Connection conn = DatabaseManager.getConnection()) {
            String update = "INSERT into gameData (whiteUsername, blackUsername, gameName, game) VALUES (default, default, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(update, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, gameName);
            stmt.setString(2, serializer.toJson(game));
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        catch (Exception e) {

        }
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String query = "SELECT * from gameData WHERE gameID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()){
                throw new DataAccessException("Error: bad request");
            }
            int id = rs.getInt("gameID");
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            String gameJSON = rs.getString("game");
            ChessGame game = new Gson().fromJson(gameJSON, ChessGame.class);

            return new GameData(id, whiteUsername, blackUsername, gameName, game);
        }
        catch (Exception e) {
            throw new DataAccessException("Error: bad request");
        }

    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if (gameData.game() != null){
                String update = "UPDATE gameData SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
                PreparedStatement stmt = conn.prepareStatement(update);
                stmt.setString(1, gameData.whiteUsername());
                stmt.setString(2, gameData.blackUsername());
                ChessGame game = gameData.game();
                Gson gson = new Gson();
                String gameJson = gson.toJson(game);
                stmt.setString(3, gameJson);
                stmt.setInt(4, gameID);
                stmt.executeUpdate();
            }
            else {
                String update = "UPDATE gameData SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
                PreparedStatement stmt = conn.prepareStatement(update);
                stmt.setString(1, gameData.whiteUsername());
                stmt.setString(2, gameData.blackUsername());
                stmt.setInt(3, gameID);
                stmt.executeUpdate();
            }

        }
        catch (Exception e) {
            throw new DataAccessException("Error: bad request");
        }
    }




    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String query = "SELECT * from gameData";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            Collection<GameData> gameList = new HashSet<>();
            while (rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameJSON = rs.getString("game");
                ChessGame game = new Gson().fromJson(gameJSON, ChessGame.class);

                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                gameList.add(gameData);
            }
            return gameList;
        }
        catch (Exception e) {
            throw new DataAccessException("Error: Could not get all games");
        }

    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String update = "TRUNCATE gameData";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            CREATE TABLE IF NOT EXISTS `gameData` (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(255) DEFAULT NULL,
              `blackUsername` varchar(255) DEFAULT NULL,
              `gameName` varchar(255) NOT NULL,
              `game` JSON NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

}
