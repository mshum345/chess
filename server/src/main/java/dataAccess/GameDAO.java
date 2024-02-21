package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection getGames();

    GameData createGame(String gameName);

    void replaceGame(GameData gameData);

    GameData getGame(int gameID);
}
