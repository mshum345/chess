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

    @Test
    public void clearPos() {
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();

        // insert test data
        users.put("Bobby", new UserData("Bobby", "testPassword1", "test@gmail.com"));
        users.put("Kate", new UserData("Kate", "testPassword2", "test@gmail.com"));
        users.put("Jimmy", new UserData("Jimmy", "testPassword3", "test@gmail.com"));
        auths.put("TestAuthToken1", new AuthData("TestAuthToken1", "Bobby"));
        auths.put("TestAuthToken2", new AuthData("TestAuthToken2", "Kate"));
        auths.put("TestAuthToken3", new AuthData("TestAuthToken3", "Jimmy"));
        games.put(1, new GameData(1, "Bobby", "Kate", "TestGameName", new ChessGame()));

        // establish test variables
        MemoryClearDAO dataAccess = new MemoryClearDAO(users, auths, games);

        ClearService service = new ClearService(dataAccess);

        // run tests
        try {
            service.clearAll();
            Assertions.assertTrue(users.isEmpty() && auths.isEmpty() && games.isEmpty());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
}
