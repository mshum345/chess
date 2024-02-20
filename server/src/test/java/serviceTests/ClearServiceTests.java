package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryClearDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import java.util.HashMap;

public class ClearServiceTests {
    private ClearService service;
    private MemoryClearDAO dataAccess;
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public void run() {
        // insert test data
        users.put(1, new UserData("Bobby", "testPassword", "test@gmail.com"));
        users.put(2, new UserData("Kate", "testPassword", "test@gmail.com"));
        users.put(3, new UserData("Jimmy", "testPassword", "test@gmail.com"));
        auths.put(1, new AuthData("TestAuthToken", "Bobby"));
        auths.put(2, new AuthData("TestAuthToken", "Kate"));
        auths.put(3, new AuthData("TestAuthToken", "Jimmy"));
        games.put(1, new GameData(1, "Bobby", "Kate", "TestGameName", new ChessGame()));

        // establish test variables
        dataAccess = new MemoryClearDAO(users, auths, games);
        service = new ClearService(dataAccess);

        // run tests
        try {
            clearAllTest();
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

    }

    public void clearAllTest() throws DataAccessException {
        service.ClearAll();

        // Checks if all data has been cleared
        if (!users.isEmpty() || !auths.isEmpty() || !games.isEmpty()) {
            throw new DataAccessException("Clear all data failed");
        }
    }
}
