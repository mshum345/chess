package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.HashMap;

public class MemoryClearDAO implements ClearDAO{
    private final HashMap<String, UserData> users;
    private final HashMap<String, AuthData> auths;
    private final HashMap<Integer, GameData> games;

    public MemoryClearDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
    public void clearDatabase() {
        users.clear();
        auths.clear();
        games.clear();
    }
}
