package dataAccess;

import java.sql.SQLException;

public class SQLClearDAO implements ClearDAO {
    public SQLClearDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    public void clearDatabase() throws DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("DELETE FROM users")) {
                statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("DELETE FROM auths")) {
                statement.executeUpdate();
            }
            try (var statement = conn.prepareStatement("DELETE FROM games")) {
                statement.executeUpdate();
            }
            // Reset auto-increment sequence for gameID column in the games table
            try (var statement = conn.prepareStatement("ALTER TABLE games AUTO_INCREMENT = 1")) {
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear database: %s", e.getMessage()));
        }
    }
}
