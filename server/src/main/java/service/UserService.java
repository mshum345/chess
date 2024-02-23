package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.ResponseData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public ResponseData register(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());

        // Check if username has already been taken
        if (checkUser != null) {
            return new ResponseData(403, "Error: already taken", null, null, null, 0);
        }

        // Generate AuthData and insert into users and auths
        var authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken, userData.username());
        userDAO.addUser(userData);
        userDAO.addAuth(authData);

        return new ResponseData(200, null, authData.username(), authData.authToken(), null, 0);
    }

    public ResponseData login(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());

        // Check if user exists and password matches
        if (checkUser == null || !checkUser.password().equals(userData.password())) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, 0);
        }

        var authToken = UUID.randomUUID().toString();
        var newAuth = new AuthData(authToken, userData.username());
        userDAO.addAuth(newAuth);

        return new ResponseData(200, null, newAuth.username(), newAuth.authToken(), null, 0);
    }

    public ResponseData logout(AuthData authData) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, 0);
        }

        userDAO.deleteAuth(authData.authToken());

        return new ResponseData(200, null, null, null, null, 0);
    }
}
