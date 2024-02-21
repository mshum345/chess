package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.HashMap;

public class MemoryClearDAO implements ClearDAO{
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public MemoryClearDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
    public void clearDatabase() throws DataAccessException {
        users.clear();
        auths.clear();
        games.clear();
    }
}
