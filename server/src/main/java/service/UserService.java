package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        var checkUser = dataAccess.getUser(userData);

        // Check if username has already been taken
        if (checkUser != null) {
            throw new DataAccessException("Error: already taken");
        }

        // Generate AuthData and insert into users and auths
        var authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken, userData.username());
        dataAccess.addUser(userData);
        dataAccess.addAuth(authData);

        return authData;
    }

    public AuthData login(UserData userData) throws DataAccessException {
        var checkUser = dataAccess.getUser(userData);
        var checkAuth = dataAccess.getAuthFromUser(userData);
        AuthData newAuth;

        // Check if user exists
        if (checkUser == null) {
            throw new DataAccessException("Error: user does not exist");
        }

        // Check if password matches
        if (!checkUser.password().equals(userData.password())) {
            throw new DataAccessException("Error: unauthorized");
        }

        // If not already logged in: create new token, if already logged in: return old token
        if (checkAuth == null) {
            var authToken = UUID.randomUUID().toString();
            newAuth = new AuthData(authToken, userData.username());
        }
        else {
            newAuth = checkAuth;
        }

        return newAuth;
    }

    public void logout(AuthData authData) throws DataAccessException {
        var checkAuth = dataAccess.getAuthFromAuth(authData);

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        dataAccess.deleteAuth(authData);
    }
}
