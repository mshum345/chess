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
            throw new DataAccessException(403, "Error: already taken");
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

        // Check if user exists
        if (checkUser == null) {
            throw new DataAccessException(500, "Error: user does not exist");
        }

        // Check if password matches
        if (!checkUser.password().equals(userData.password())) {
            throw new DataAccessException(401, "Error: unauthorized");
        }

        var authToken = UUID.randomUUID().toString();
        var newAuth = new AuthData(authToken, userData.username());
        userDAO.addAuth(newAuth);

        return newAuth;
    }

    public void logout(AuthData authData) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        if (checkAuth == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        }

        userDAO.deleteAuth(authData.authToken());
    }
}
