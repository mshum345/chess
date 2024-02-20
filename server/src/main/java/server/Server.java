package server;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.HashMap;

public class Server {
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;
    private HashMap<Integer, UserData> users;
    private HashMap<Integer, AuthData> auths;
    private HashMap<Integer, GameData> games;

    public Server() {
        // Memory or SQL? Where do we determine between the two options?
        clearService = new ClearService(new MemoryClearDAO(users, auths, games));
        userService = new UserService(new MemoryUserDAO(users, auths, games));
        gameService = new GameService(new MemoryGameDAO(users, auths, games));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
