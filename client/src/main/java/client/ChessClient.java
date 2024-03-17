package client;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class ChessClient {
    private final String serverUrl;
    private boolean state = false;
    public ChessClient(String url) {
        this.serverUrl = url;
    }

    public String preHelp() {
        return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - quit playing chess
                    help - help with possible commands
                    """;
    }

    public String postHelp() {
        return """
                create <GAMENAME> - to create a new game
                list - lists all games
                join <ID> [WHITE|BLACK|<empty>] - joins a game as a player
                observe <ID> - joins a game as an observer
                logout - logs you out
                quit - quit playing chess
                help - help with possible commands
                """;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == false) {
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
                    case "create game" -> createGame(params);
                    case "list games" -> listGames(params);
                    case "join game" -> joinGame(params);
                    case "join observer" -> joinObserver(params);
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
        http.setRequestProperty("Content-Type", "application/json");
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
            state = true;
            return "Register User Success";
        } else {
            return "Failed to register. HTTP error code: " + responseCode;
        }
    }

    public String login(String... params) throws Exception {
        if (params.length >= 1) {
            state = true;
            var visitorName = String.join("-", params);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Expected: <yourname>");
    }

    public String logout(String... params) throws Exception {
        return "";
    }

    private String createGame(String[] params) {
        return "";
    }

    private String listGames(String[] params) {
        return "";
    }

    private String joinGame(String[] params) {
        return "";
    }

    private String joinObserver(String[] params) {
        return "";
    }

    public boolean getState() {
        return state;
    }
}
