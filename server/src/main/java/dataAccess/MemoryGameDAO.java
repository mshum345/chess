package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData> games;
    private int nextID = 0;

    public MemoryGameDAO(HashMap<Integer, GameData> games) {
        this.games = games;
    }

    public Collection getGames() {
        return games.values();
    }

    public GameData createGame(String gameName) {
        nextID++;
        var newGame = new GameData(nextID, null, null, gameName, new ChessGame());
        games.put(nextID, newGame);
        return newGame;
    }

    public void replaceGame(GameData gameData) {
        games.replace(gameData.gameID(), gameData);
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }
}
