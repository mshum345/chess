package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData) throws DataAccessException;

    void addUser(UserData userData) throws DataAccessException;

    void addAuth(AuthData authData) throws DataAccessException;

    AuthData getAuthFromUser(UserData userData);

    AuthData getAuthFromAuth(AuthData authData);

    void deleteAuth(AuthData authData);
}
