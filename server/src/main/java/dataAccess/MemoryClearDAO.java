package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.util.HashMap;

public class MemoryClearDAO implements ClearDAO{
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    int id;
    public void clearDatabase() throws DataAccessException {
        users.clear();
        auths.clear();
        games.clear();
    }

    public HashMap<Integer, UserData> getAllUsers() {
        return users;
    }

    public HashMap<Integer, AuthData> getAllAuths() {
        return auths;
    }

    public HashMap<Integer, GameData> getAllGames() {
        return games;
    }


}
