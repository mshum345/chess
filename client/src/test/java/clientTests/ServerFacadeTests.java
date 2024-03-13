package clientTests;

import client.ChessClient;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ChessClient client;

    @BeforeAll
    public static void init() {
        server = new Server();
        // TODO Clear database
        var port = server.run(0);
        var url = "http://localhost:" + server.port();
        client = new ChessClient(url);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void register() throws Exception {
        var authData = client.register("player1", "password", "p1@email.com");
    }

}
