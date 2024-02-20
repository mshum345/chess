package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    String getUser(UserData userData) throws DataAccessException;

    void addUser(UserData userData) throws DataAccessException;

    void addAuth(AuthData authData) throws DataAccessException;
}
