package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws DataAccessException;

    void addUser(UserData userData) throws DataAccessException;

    void addAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    void deleteAuth(String username) throws DataAccessException;
}
