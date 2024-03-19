package clientTests;

import client.ChessClient;
import com.google.gson.Gson;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.ResponseData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ChessClient client;
    private static SQLUserDAO userDAO;
    private static SQLGameDAO gameDAO;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        var port = server.run(0);
        var url = "http://localhost:" + server.port();
        client = new ChessClient(url);
        client.clearDatabase();
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertNotNull(userDAO.getUser("testUser1"));
    }

    @Test
    public void registerNeg() throws Exception {
        client.clearDatabase();
        Assertions.assertThrows(Exception.class, () -> {
            client.register(null, null, null); // Pass null parameters
        });
    }

    @Test
    public void loginPos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertEquals("Login Success.", client.login("testUser1", "testPass1"));
    }

    @Test
    public void loginNeg() throws Exception {
        client.clearDatabase();
        Assertions.assertThrows(Exception.class, () -> {
            client.login(null, null); // Pass null parameters
        });
    }

    @Test
    public void logoutPos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertEquals("Logout Success.", client.logout());
    }

    @Test
    public void logoutNeg() throws Exception {
        client.clearDatabase();
        Assertions.assertEquals("Failed to logout. HTTP error code: 401", client.logout());
    }

}
