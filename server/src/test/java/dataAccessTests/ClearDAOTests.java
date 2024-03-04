package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLClearDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class ClearDAOTests {

    @Test
    public void ClearAllPos() {
        // Insert dummy data
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES ('testUser', 'testpass', 'testEmail')")) {
                var ps = statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES ('testAuth', 'testUser')")) {
                var ps = statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES ('testUser1', 'testUser2', 'testName', 'testGame')")) {
                var ps = statement.executeUpdate();
            }
        }
        catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }

        // Run methods
        try {
            var clearDAO = new SQLClearDAO();
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
}
