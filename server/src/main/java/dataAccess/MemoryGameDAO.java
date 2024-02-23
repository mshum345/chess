package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;
    private int nextID = 0;

    public MemoryGameDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
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
