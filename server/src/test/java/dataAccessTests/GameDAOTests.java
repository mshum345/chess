package dataAccessTests;

import dataAccess.DatabaseManager;
import dataAccess.SQLClearDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameDAOTests {
    private SQLClearDAO clearDAO;
    private SQLGameDAO gameDAO;

    GameDAOTests() {
        try {
            clearDAO = new SQLClearDAO();
            gameDAO = new SQLGameDAO();
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void getGamesPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            insertDummyData();

            // Run Tests
            Assertions.assertNotNull(gameDAO.getGames());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void getGamesNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();

            // Run Tests
            Assertions.assertNull(gameDAO.getGames());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void createGamePos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();

            // Run Tests
            Assertions.assertNotNull(gameDAO.createGame("testName"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void createGameNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();

            // Run Tests
            Assertions.assertNotEquals(gameDAO.createGame("testName").gameName(), "badName");
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void replaceGamePos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            insertDummyData();

            // Run Tests
            gameDAO.replaceGame(new GameData(1, "testWhite", null, "testName", null));
            Assertions.assertNotNull(gameDAO.getGame(1).whiteUsername());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void replaceGameNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();

            try {
                gameDAO.replaceGame(new GameData(1, "testWhite", null, "testName", null));
            } catch (Throwable ex) {
            }
            // Run Tests
            Assertions.assertNull(gameDAO.getGame(1).whiteUsername());
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void getGamePos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            insertDummyData();

            // Run Tests
            Assertions.assertNotNull(gameDAO.getGame(1));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void getGameNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();

            // Run Tests
            Assertions.assertNull(gameDAO.getGame(1));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }



    public void insertDummyData() {
        // Insert dummy data
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES ('testUser1', 'testUser2', 'testName', 'testGame')")) {
                var ps = statement.executeUpdate();
            }
        }
        catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
}
