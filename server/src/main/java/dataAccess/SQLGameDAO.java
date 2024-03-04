package dataAccess;

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

    public GameData createGame(String gameName) {
        return null;
    }

    public void replaceGame(GameData gameData) {

    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM games WHERE gameID=?")) {

            ps.setInt(1, gameID);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var json = rs.getString("json");
                return new Gson().fromJson(json, GameData.class);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get game: %s", e.getMessage()));
        }
        return null;
    }
}
