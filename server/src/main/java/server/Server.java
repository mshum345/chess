package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.ResponseData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.HashMap;

public class Server {
    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    private ResponseData responseData;

    public Server() {
        HashMap<String, UserData> users = new HashMap<>();
        HashMap<String, AuthData> auths = new HashMap<>();
        HashMap<Integer, GameData> games = new HashMap<>();
        var clearDAO = new MemoryClearDAO(users, auths, games);
        var userDAO = new MemoryUserDAO(users, auths);
        var gameDAO = new MemoryGameDAO(games);
        clearService = new ClearService(clearDAO);
        userService = new UserService(userDAO);
        gameService = new GameService(gameDAO, userDAO);
    }

    public static void main(String[] args) {
        new Server().run(8080);
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
        responseData = clearService.clearAll();
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    private Object register(Request req, Response res) throws DataAccessException {
        // Extract data from req body
        var userData = new Gson().fromJson(req.body(), UserData.class);

        // Make service call
        responseData = userService.register(userData);
        res.status(responseData.status());

        return new Gson().toJson(responseData);
    }

    private Object login(Request req, Response res) throws DataAccessException {
        // Extract data from req body
        var userData = new Gson().fromJson(req.body(), UserData.class);

        // Make service call
        responseData = userService.login(userData);
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    private Object logout(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Make service call
        responseData = userService.logout(authData);
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    private Object listGames(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Make service call
        responseData = gameService.listGames(authData);
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    private Object createGame(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Extract data from req body
        JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
        JsonElement gameNameElement = requestBody.get("gameName");
        String gameName = (gameNameElement != null && !gameNameElement.isJsonNull()) ? gameNameElement.getAsString() : null;

        // Make service call
        responseData = gameService.createGame(authData, gameName);
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    private Object joinGame(Request req, Response res) throws DataAccessException {
        // Extract data from req headers
        var authData = new AuthData(req.headers("authorization"), null);

        // Extract data from req body
        JsonObject requestBody = new Gson().fromJson(req.body(), JsonObject.class);
        JsonElement gameNameElement = requestBody.get("playerColor");
        String playerColor = (gameNameElement != null && !gameNameElement.isJsonNull()) ? gameNameElement.getAsString() : null;
        int gameID = requestBody.get("gameID").getAsInt();

        // Make service call
        responseData = gameService.joinGame(authData, playerColor, gameID);
        res.status(responseData.status());
        return new Gson().toJson(responseData);
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(500);
    }
}