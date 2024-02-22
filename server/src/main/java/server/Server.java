package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private HashMap<String, UserData> users = new HashMap<>();
    private HashMap<String, AuthData> auths  = new HashMap<>();
    private HashMap<Integer, GameData> games  = new HashMap<>();
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

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request req, Response res) throws DataAccessException {
        // Make service call
        clearService.ClearAll();
        res.status(200);
        return "";
    }

    private Object register(Request req, Response res) throws DataAccessException {
        // Extract data from req body
        var userData = new Gson().fromJson(req.body(), UserData.class);

        // Make service call
        var authData = userService.register(userData);
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        // Extract data from req body
        var userData = new Gson().fromJson(req.body(), UserData.class);

        // Make service call
        var authData = userService.login(userData);
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Make service call
        userService.logout(authData);
        res.status(200);
        return "";
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Make service call
        var list = gameService.listGames(authData).toArray();
        res.type("application/json");
        res.status(200);
        return new Gson().toJson(Map.of("game", list));
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Extract data from req body
        JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
        String gameName = requestBody.get("gameName").getAsString();

        // Make service call
        var newGame = gameService.createGame(authData, gameName);
        var gameID = newGame.gameID();
        res.status(200);
        return new Gson().toJson(Map.of("gameID", gameID));
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Extract data from req body
        JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
        String playerColor = requestBody.get("playerColor").getAsString();
        int gameID = requestBody.get("gameID").getAsInt();

        // Make service call
        gameService.joinGame(authData, playerColor, gameID);
        res.status(200);
        return "";
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(ex.StatusCode());
    }
}
