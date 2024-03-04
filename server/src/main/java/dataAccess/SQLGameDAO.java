package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    public Collection getGames() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM games")) {

            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gameId = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");

                    // Create a new GameData object for each row and add it to the list
                    games.add(new GameData(gameId, whiteUsername, blackUsername, gameName, null));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get all games: %s", e.getMessage()));
        }
        return games;
    }

    public GameData createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (null, null, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

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

    public void replaceGame(GameData gameData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {

            var gameJSON = new Gson().toJson(gameData.game());
            ps.setString(1, gameData.whiteUsername());
            ps.setString(2, gameData.blackUsername());
            ps.setString(3, gameData.gameName());
            ps.setString(4, gameJSON);
            ps.setInt(5, gameData.gameID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update game: %s", e.getMessage()));
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM games WHERE gameID=?")) {

            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String fetchedWhiteUsername = rs.getString("whiteUsername");
                String fetchedBlackUsername = rs.getString("blackUsername");
                String fetchedGameName = rs.getString("gameName");
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
