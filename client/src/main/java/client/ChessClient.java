package client;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class ChessClient {
    private final String serverUrl;
    private boolean loggedIn = false;
    private String authToken;
    public ChessClient(String url) {
        this.serverUrl = url;
    }

    public String preHelp() {
        return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - quit playing chess
                    help - help with possible commands""";
    }

    public String postHelp() {
        return """
                create <GAMENAME> - to create a new game
                list - lists all games
                join <ID> [WHITE|BLACK|<empty>] - joins a game as a player
                observe <ID> - joins a game as an observer
                logout - logs you out
                quit - quit playing chess
                help - help with possible commands""";
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (loggedIn == false) {
                return switch (cmd) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> preHelp();
                };
            }
            else {
                return switch (cmd) {
                    case "logout" -> logout(params);
                    case "create" -> createGame(params);
                    case "list" -> listGames(params);
                    case "join" -> joinGame(params);
                    case "observe" -> joinGame(params);
                    case "quit" -> "quit";
                    default -> postHelp();
                };
            }
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    public String clearDatabase() throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/db");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("DELETE");

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Clear Database Success";
        } else {
            return "Failed to clear database. HTTP error code: " + responseCode;
        }
    }

    public String register(String... params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        // Write out the body
        var body = Map.of("username", params[0], "password", params[1], "email", params[2]);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response body
            try (InputStream inputStream = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Map<String, String> responseMap = new Gson().fromJson(reader, Map.class);
                authToken = responseMap.get("authToken");
                loggedIn = true;
                return "Register Success.";
            }
        } else {
            return "Failed to register. HTTP error code: " + responseCode;
        }
    }

    public String login(String... params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        // Write out the body
        var body = Map.of("username", params[0], "password", params[1]);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Map<String, String> responseMap = new Gson().fromJson(reader, Map.class);
                authToken = responseMap.get("authToken");
                loggedIn = true;
                return "Login Success.";
            }
        } else {
            return "Failed to login. HTTP error code: " + responseCode;
        }
    }

    public String logout(String... params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("DELETE");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            loggedIn = false;
            return "Logout Success";
        } else {
            return "Failed to logout. HTTP error code: " + responseCode;
        }
    }

    private String createGame(String[] params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("POST");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        // Write out the body
        var body = Map.of("gameName", params[0]);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Game Creation Success.";
        } else {
            return "Failed to create game. HTTP error code: " + responseCode;
        }
    }

    private String listGames(String[] params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("GET");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response body
            try (InputStream inputStream = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Map<String, String> responseMap = new Gson().fromJson(reader, Map.class);
                return "Games: " + responseMap;
            }
        } else {
            return "Failed to list games. HTTP error code: " + responseCode;
        }
    }

    private String joinGame(String[] params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("PUT");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        // Handle join observer
        var playerColor = "";
        if (params.length == 2) {
            playerColor = params[1];
        }

        // Write out the body
        var body = Map.of("gameID", params[0], "playerColor", playerColor);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return "Join Game Success.";
        } else {
            return "Failed to Join Game. HTTP error code: " + responseCode;
        }
    }

    public boolean getState() {
        return loggedIn;
    }
}
