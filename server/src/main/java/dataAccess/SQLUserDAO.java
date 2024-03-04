package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM users WHERE username=?")) {

            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                // Extract data from the result set
                String fetchedUsername = rs.getString("username");
                String fetchedHashedPassword = rs.getString("password");
                String fetchedEmail = rs.getString("email");
                return new UserData(fetchedUsername, fetchedHashedPassword, fetchedEmail);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get user: %s", e.getMessage()));
        }
        return null;
    }

    public void addUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(userData.password());

                statement.setString(1, userData.username());
                statement.setString(2, hashedPassword);
                statement.setString(3, userData.email());
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to add user: %s", e.getMessage()));
        }
    }

    public void addAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)")) {

                statement.setString(1, authData.authToken());
                statement.setString(2, authData.username());
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to add auth: %s", e.getMessage()));
        }
    }

    public AuthData getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("SELECT * FROM auths WHERE username=?")) {

            ps.setString(1, username);
            var rs = ps.executeQuery();
            if (rs.next()) {
                String fetchedAuthToken = rs.getString("authToken");
                String fetchedUsername = rs.getString("username");
                return new AuthData(fetchedAuthToken, fetchedUsername);
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get auth: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement("DELETE * FROM auths WHERE username=?")) {

            ps.setString(1, username);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to delete auth: %s", e.getMessage()));
        }
    }
}
