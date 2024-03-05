package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

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
            String update = "INSERT (gameName, game) into gameData";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, gameName);
            stmt.setString(2, serializer.toJson(game));
            stmt.executeUpdate();
            
        }
        catch (Exception e) {

        }

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public Collection<GameData> getAllGames() {
        return null;
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            String update = "TRUNCATE gameData";
            PreparedStatement stmt = conn.prepareStatement(update);
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
              `whiteUsername` varchar(255),
              `blackUsername` varchar(255),
              `gameName` varchar(255) NOT NULL,
              `game` JSON NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };

}
