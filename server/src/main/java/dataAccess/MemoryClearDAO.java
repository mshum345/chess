package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.HashMap;

public class MemoryClearDAO implements ClearDAO{
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public MemoryClearDAO(HashMap<Integer, UserData> users, HashMap<Integer, AuthData> auths, HashMap<Integer, GameData> games) {
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
