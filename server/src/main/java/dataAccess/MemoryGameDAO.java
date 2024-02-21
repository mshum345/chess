package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }

    public Collection getGames() {
        return games.values();
    }
}
