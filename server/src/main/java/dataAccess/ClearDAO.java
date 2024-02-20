package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public interface ClearDAO {
    void clearDatabase() throws DataAccessException;
}
