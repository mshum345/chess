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
                var json = rs.getString("json");
                return new Gson().fromJson(json, UserData.class);
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
                statement.executeQuery();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("unable to clear database: %s", e.getMessage()));
        }
    }

    public void addAuth(AuthData authData) throws DataAccessException {

    }

    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String username) throws DataAccessException {

    }
}
