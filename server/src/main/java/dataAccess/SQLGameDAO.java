package dataAccess;

import model.GameData;

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

    public GameData getGame(int gameID) {
        return null;
    }
}
