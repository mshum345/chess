package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> users;
    private final HashMap<String, AuthData> auths;

    public MemoryUserDAO(HashMap<String, UserData> users, HashMap<String, AuthData> auths) {
        this.users = users;
        this.auths = auths;
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
