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
    private HashMap<String, UserData> users;
    private HashMap<String, AuthData> auths;
    private HashMap<Integer, GameData> games;
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;

    public Server() {
        var clearDAO = new MemoryClearDAO(users, auths, games);
        var userDAO = new MemoryUserDAO(users, auths, games);
        var gameDAO = new MemoryGameDAO(users, auths, games);
        clearService = new ClearService(clearDAO);
        userService = new UserService(userDAO);
        gameService = new GameService(gameDAO, userDAO);
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
