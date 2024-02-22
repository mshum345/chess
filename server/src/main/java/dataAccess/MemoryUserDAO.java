package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;
    int nextID = 1;

    public MemoryUserDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }

    public void addUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void addAuth(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }
}
