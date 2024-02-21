package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());

        // Check if username has already been taken
        if (checkUser != null) {
            throw new DataAccessException("Error: already taken");
        }

        // Generate AuthData and insert into users and auths
        var authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken, userData.username());
        userDAO.addUser(userData);
        userDAO.addAuth(authData);

        return authData;
    }

    public AuthData login(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());
        var checkAuth = userDAO.getAuth(userData.username());
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
        var checkAuth = userDAO.getAuth(authData.username());

        if (checkAuth == null) {
            throw new DataAccessException("Error: unauthorized");
        }

        userDAO.deleteAuth(authData.username());
    }
}
