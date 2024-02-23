package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.ResponseData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.util.HashMap;

public class GameServiceTests {
    private GameService gameService;
    private MemoryGameDAO gameDAO;
    private MemoryUserDAO userDAO;
    private ResponseData testRes;

    @Test
    public void listPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        games.put(1, new GameData(1, "Bobby", "Kate", "TestGameName", new ChessGame()));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            testRes = gameService.listGames(new AuthData("testAuth1", "testUser1"));
            Assertions.assertEquals(1, testRes.games().size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void listNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        games.put(1, new GameData(1, "Bobby", "Kate", "TestGameName", new ChessGame()));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            testRes = gameService.listGames(new AuthData("badAuth", "testUser1"));
            Assertions.assertNull(testRes.games());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void createPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            gameService.listGames(new AuthData("testAuth1", "testUser1"));
            Assertions.assertEquals(1, games.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void createNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            gameService.listGames(new AuthData("badAuth", "testUser1"));
            Assertions.assertEquals(0, games.size());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void joinPos() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        games.put(1, new GameData(1, "", "", "TestGameName", new ChessGame()));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            gameService.joinGame(new AuthData("testAuth1", "testUser1"), "WHITE", 1);
            Assertions.assertEquals("testUser1", games.get(1).whiteUsername());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void joinNeg() {
        // establish test variables
        var users = new HashMap<String, UserData>();
        var auths = new HashMap<String, AuthData>();
        var games = new HashMap<Integer, GameData>();
        auths.put("testAuth1", new AuthData("testAuth1", "testUser1"));
        games.put(1, new GameData(1, "", "", "TestGameName", new ChessGame()));
        gameDAO = new MemoryGameDAO(games);
        userDAO = new MemoryUserDAO(users, auths);
        gameService = new GameService(gameDAO, userDAO);

        // run test
        try {
            gameService.joinGame(new AuthData("badAuth", "testUser1"), "WHITE", 1);
            Assertions.assertEquals("", games.get(1).whiteUsername());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
}
