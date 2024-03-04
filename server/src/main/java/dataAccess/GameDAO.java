package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection getGames() throws DataAccessException;

    GameData createGame(String gameName) throws DataAccessException;

    void replaceGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;
}
