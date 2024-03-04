package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    public Collection getGames() {
        return null;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (null, null, ?, ?)")) {

            var game = new Gson().toJson(new ChessGame());
            ps.setString(1, gameName);
            ps.setString(2, game);
            ps.executeUpdate();

            try (var generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int gameId = generatedKeys.getInt(1);
                    return getGame(gameId);
                }
                else {
                    return null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to create game: %s", e.getMessage()));
        }
    }

    public void replaceGame(GameData gameData) {

    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM games WHERE gameID=?")) {

            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String fetchedWhiteUsername = rs.getString("whiteUsername");
                String fetchedBlackUsername = rs.getString("blackUsername");
                String fetchedGameName = rs.getString("blackUsername");
                ChessGame fetchedGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameID,fetchedWhiteUsername, fetchedBlackUsername, fetchedGameName, fetchedGame);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get game: %s", e.getMessage()));
        }
        return null;
    }
}
