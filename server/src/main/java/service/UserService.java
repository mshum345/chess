package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO dataAccess;
    private UserData userData;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register() throws DataAccessException {
        var checkUser = dataAccess.getUser(userData);

        if (checkUser != null) {
            throw new DataAccessException("Error: already taken");
        }

        var authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken, userData.username());
        dataAccess.addUser(userData);
        dataAccess.addAuth(authData);

        return authData;
    }
}
