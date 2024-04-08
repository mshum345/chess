package clientTests;

import client.ChessClient;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
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
        client = new ChessClient(url, null);
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

    @Test
    public void createGamePos() throws Exception {
        client.clearDatabase();
        client.createGame("testGame1");
        Assertions.assertNotNull(gameDAO.getGames());
    }

    @Test
    public void createGameNeg() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertThrows(Exception.class, () -> {
            client.createGame(null); // Pass null parameters
        });
    }

    @Test
    public void listGamesPos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        client.createGame("testGame1");
        Assertions.assertNotNull(client.listGames());
    }

    @Test
    public void listGamesNeg() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertEquals("Games:", client.listGames().trim());
    }

    @Test
    public void joinGamePos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        client.createGame("testGame1");
        Assertions.assertEquals("Join Game Success.", client.joinGame("1", "WHITE"));
    }

    @Test
    public void joinGameNeg() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertEquals("Failed to Join Game or returned to previously joined game. HTTP code: 400", client.joinGame("1", "WHITE"));
    }

    @Test
    public void observePos() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        client.createGame("testGame1");
        Assertions.assertEquals("Join Game Success.", client.joinGame("1", ""));
    }

    @Test
    public void observeNeg() throws Exception {
        client.clearDatabase();
        client.register("testUser1", "testPass1", "testEmail");
        Assertions.assertEquals("Failed to Join Game or returned to previously joined game. HTTP code: 400", client.joinGame("1", "WHITE"));
    }
}
