package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;
    int nextID = 1;

    public MemoryUserDAO(HashMap<Integer, UserData> users, HashMap<Integer, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }



    public void addUser(UserData userData) {
        users.put(nextID++, userData);
    }

    public UserData getUser(UserData userData) {
        for (var user : users.values()) {
            if (user.username().equals(userData.username())) {
                return user; // Return the matched username
            }
        }
        return null; // Return null if no match found
    }

    public void addAuth(AuthData authData) {

    }

    public AuthData getAuthFromUser(UserData userData) {
        return null;
    }

    public AuthData getAuthFromAuth(AuthData authData) {
        return null;
    }

    public void deleteAuth(AuthData authData) {

    }
}
