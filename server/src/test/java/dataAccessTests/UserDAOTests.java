package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLClearDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDAOTests {

    private SQLClearDAO clearDAO;
    private SQLUserDAO userDAO;

    UserDAOTests() {
        try {
            clearDAO = new SQLClearDAO();
            userDAO = new SQLUserDAO();
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }
    @Test
    public void GetUserPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();

            // Run Tests
            Assertions.assertNotNull(userDAO.getUser("testUser"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void GetUserNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();

            // Run Tests
            Assertions.assertNull(userDAO.getUser("badUser"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void AddUserPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            userDAO.addUser(new UserData("testUser", "testPass", "testEmail"));

            // Run Tests
            Assertions.assertNotNull(userDAO.getUser("testUser"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void AddUserNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            try {
                userDAO.addUser(new UserData("testUser", null, null));
            } catch (Throwable ex) {
            }

            // Run Tests
            Assertions.assertNull(userDAO.getUser("testUser"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void GetAuthPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();

            // Run Tests
            Assertions.assertNotNull(userDAO.getAuth("testAuth"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void GetAuthNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();

            // Run Tests
            Assertions.assertNull(userDAO.getAuth("badUser"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void AddAuthPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            userDAO.addAuth(new AuthData("testUser", "testAuth"));

            // Run Tests
            Assertions.assertNotNull(userDAO.getAuth("testAuth"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void AddAuthNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            try {
                userDAO.addAuth(new AuthData(null, "testUser"));
            } catch (Throwable ex) {
            }

            // Run Tests
            Assertions.assertNull(userDAO.getAuth("testAuth"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void DeleteAuthPos() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();
            try {
                userDAO.deleteAuth("testAuth");
            } catch (Throwable ex) {
            }

            // Run Tests
            Assertions.assertNull(userDAO.getAuth("testAuth"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    @Test
    public void DeleteAuthNeg() {
        // Run methods
        try {
            // Test Prep
            clearDAO.clearDatabase();
            InsertDummyData();
            try {
                userDAO.deleteAuth("badAuth");
            } catch (Throwable ex) {
            }

            // Run Tests
            Assertions.assertNotNull(userDAO.getAuth("testAuth"));
        } catch (Throwable ex) {
            System.out.printf("Test Error: %s%n", ex.getMessage());
        }
    }

    public void InsertDummyData() {
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
    }
}
