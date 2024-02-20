package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public MemoryGameDAO(HashMap<Integer, UserData> users, HashMap<Integer, AuthData> auths, HashMap<Integer, GameData> games) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }
}
