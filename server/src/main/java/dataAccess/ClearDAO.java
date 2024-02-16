package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public interface ClearDAO {

    void clearDatabase() throws DataAccessException;

    HashMap<Integer, UserData> getAllUsers();
    HashMap<Integer, AuthData> getAllAuths();
    HashMap<Integer, GameData> getAllGames();
}
