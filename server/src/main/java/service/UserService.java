package service;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.ResponseData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public ResponseData register(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());

        // Checks if bad request
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            return new ResponseData(400, "Error: bad request", null, null, null, null);
        }

        // Check if username has already been taken
        if (checkUser != null) {
            return new ResponseData(403, "Error: already taken", null, null, null, null);
        }

        // Generate AuthData and insert into users and auths
        var authToken = UUID.randomUUID().toString();
        var authData = new AuthData(authToken, userData.username());
        userDAO.addUser(userData);
        userDAO.addAuth(authData);

        return new ResponseData(200, null, authData.username(), authData.authToken(), null, null);
    }

    public ResponseData login(UserData userData) throws DataAccessException {
        var checkUser = userDAO.getUser(userData.username());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Check if user exists and password matches
        if (checkUser == null || !encoder.matches(userData.password(), checkUser.password())) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }

        var authToken = UUID.randomUUID().toString();
        var newAuth = new AuthData(authToken, userData.username());
        userDAO.addAuth(newAuth);

        return new ResponseData(200, null, newAuth.username(), newAuth.authToken(), null, null);
    }

    public ResponseData logout(AuthData authData) throws DataAccessException {
        var checkAuth = userDAO.getAuth(authData.authToken());

        if (checkAuth == null) {
            return new ResponseData(401, "Error: unauthorized", null, null, null, null);
        }

        userDAO.deleteAuth(authData.authToken());

        return new ResponseData(200, null, null, null, null, null);
    }
}
