package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public MemoryUserDAO(HashMap<Integer, UserData> users, HashMap<Integer, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }

    public String getUser(UserData userData) {
        return null;
    }

    public void addUser(UserData userData) {

    }

    public void addAuth(AuthData authData) {

    }
}
