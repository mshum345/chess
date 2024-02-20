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
}
