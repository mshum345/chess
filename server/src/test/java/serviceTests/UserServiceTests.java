package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.ResponseData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.HashMap;

public class UserServiceTests {
    private UserService userService;
    private MemoryUserDAO userDAO;

    @Test
    public void regPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.register(new UserData("testUser1", "testPassword4", "test@gmail.com"));
            Assertions.assertNotNull(users.get("testUser1"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void regNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.register(new UserData("testUser1", "testPassword4", "test@gmail.com"));
            userService.register(new UserData("testUser1", "testPassword4", "test@gmail.com"));
            Assertions.assertEquals(1, auths.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void loginPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        users.put("testUser1", new UserData("testUser1", "testPass1", "test@gmail.com"));
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.login(new UserData("testUser1", "testPass1", "test@gmail.com"));
            Assertions.assertEquals(1, auths.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void loginNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        users.put("testUser1", new UserData("testUser1", "testPass1", "test@gmail.com"));
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.login(new UserData("testUser1", "badPass", "test@gmail.com"));
            Assertions.assertEquals(0, auths.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void logoutPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.logout(new AuthData("testAuth1", "testUser1"));
            Assertions.assertEquals(0, auths.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void logoutNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        userDAO = new MemoryUserDAO(users, auths, games);
        userService = new UserService(userDAO);

        // run test
        try {
            userService.logout(new AuthData("badAuth", "testUser1"));
            Assertions.assertEquals(1, auths.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
}
