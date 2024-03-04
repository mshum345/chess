package dataAccessTests;

import dataAccess.DatabaseManager;
import dataAccess.SQLClearDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClearDAOTests {

    @Test
    public void ClearAllPos() {
        // Run methods
        try {
            var clearDAO = new SQLClearDAO();
            InsertDummyData();
            clearDAO.clearDatabase();
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }

        // Run Tests
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT * FROM users")) {
                var ps = statement.executeQuery();
                Assertions.assertFalse(ps.next());
            }
            try (var statement = conn.prepareStatement("SELECT * FROM auths")) {
                var ps = statement.executeQuery();
                Assertions.assertFalse(ps.next());
            }
            try (var statement = conn.prepareStatement("SELECT * FROM games")) {
                var ps = statement.executeQuery();
                Assertions.assertFalse(ps.next());
            }
        }
        catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    public void InsertDummyData() {
        // Insert dummy data
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES ('testUser', 'testpass', 'testEmail')")) {
                statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES ('testAuth', 'testUser')")) {
                statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES ('testUser1', 'testUser2', 'testName', 'testGame')")) {
                statement.executeUpdate();
            }
        }
        catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
}
