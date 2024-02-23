package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryClearDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;
import java.util.HashMap;

public class ClearServiceTests {
    private ClearService service;
    private MemoryClearDAO dataAccess;
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;

    @Test
    public void run() {
        // insert test data
        users.put("Bobby", new UserData("Bobby", "testPassword", "test@gmail.com"));
        users.put("Kate", new UserData("Kate", "testPassword", "test@gmail.com"));
        users.put("Jimmy", new UserData("Jimmy", "testPassword", "test@gmail.com"));
        auths.put("Bobby", new AuthData("TestAuthToken", "Bobby"));
        auths.put("Kate", new AuthData("TestAuthToken", "Kate"));
        auths.put("Jimmy", new AuthData("TestAuthToken", "Jimmy"));
        games.put(1, new GameData(1, "Bobby", "Kate", "TestGameName", new ChessGame()));

        // establish test variables
        dataAccess = new MemoryClearDAO(users, auths, games);
        service = new ClearService(dataAccess);

        // run tests
        try {
            service.ClearAll();
            Assertions.assertTrue(!users.isEmpty() || !auths.isEmpty() || !games.isEmpty());
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}
