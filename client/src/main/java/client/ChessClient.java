package client;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.webSocket.NotificationHandler;
import client.webSocket.WebSocketFacade;
import com.google.gson.Gson;
import model.ResponseData;
import model.GameData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChessClient {
    private WebSocketFacade ws;
    private String username;
    private final String serverUrl;
    private String authToken;
    private int currentGameID;
    Map<Integer, GameData> gameMap;
    VisualChessBoard board;
    private UserState state;
    private final NotificationHandler notificationHandler;
    public enum UserState {
        LOGGED_OUT,
        LOGGED_IN,
        PLAYER,
        OBSERVER
    }

    public ChessClient(String url, NotificationHandler notificationHandler) {
        this.serverUrl = url;
        this.state = UserState.LOGGED_OUT;
        this.board = new VisualChessBoard();
        this.notificationHandler = notificationHandler;
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

    public String playerHelp() {
        return """
                redraw - redraws the current chess board
                leave - leaves the game
                move <start row> <start column> <end row> <end collumn> <promotion piece (write "none" if not applicable)> - makes a move in chess
                resign - resigns you from the game
                highlight <row> <column> - highlights all legal moves for a piece
                help - help with possible commands""";
    }

    public String obsHelp() {
        return """
                redraw - redraws the current chess board
                leave - leaves the game
                highlight <row> <column> - highlights all legal moves for a piece
                help - help with possible commands""";
    }

    public String eval(String input) throws Exception {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (state == UserState.LOGGED_IN) {
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> joinGame(params);
                case "quit" -> "quit";
                default -> postHelp();
            };
        } else if (state == UserState.PLAYER) {
            return switch (cmd) {
                case "redraw" -> drawCurrentBoard(params);
                case "leave" -> leave(params);
                case "move" -> makeMove(params);
                case "resign" -> resign(params);
                case "highlight" -> legalMoves(params);
                default -> playerHelp();
            };
        } else if (state == UserState.OBSERVER) {
            return switch (cmd) {
                case "redraw" -> drawCurrentBoard(params);
                case "leave" -> leave(params);
                case "highlight" -> legalMoves(params);
                default -> obsHelp();
            };
        } else {
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> preHelp();
            };
        }
    }

    private String legalMoves(String[] params) throws Exception {
        ws.drawLegalMoves(params[0], params[1]);
        return "";
    }

    private String resign(String[] params) {
        state = UserState.LOGGED_IN;
        ws.resign(authToken, currentGameID);
        return "resigned from game";
    }

    private String makeMove(String[] params) {
        ws.makeMove(authToken, currentGameID, params);
        return "";
    }

    private String leave(String[] params) {
        state = UserState.LOGGED_IN;
        ws.leave(authToken, currentGameID);
        return "left game";
    }

    private String drawCurrentBoard(String[] params) {
        ws.drawCurrentBoard();
        return "";
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
                state = UserState.LOGGED_IN;
                username = params[0];
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
                state = UserState.LOGGED_IN;
                username = params[0];
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
            state = UserState.LOGGED_OUT;
            username = "";
            return "Logout Success.";
        } else {
            return "Failed to logout. HTTP error code: " + responseCode;
        }
    }

    public String createGame(String... params) throws Exception {
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

    public String listGames(String... params) throws Exception {
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
                ResponseData responseData = new Gson().fromJson(reader, ResponseData.class);

                // Extract games and convert to map
                Map<Integer, GameData> newGameMap = new HashMap<>();
                int i = 1;
                for (GameData gameData : responseData.games()) {
                    newGameMap.put(i++, gameData);
                }

                gameMap = newGameMap;

                // Construct string with game details using gameMap
                StringBuilder sb = new StringBuilder();
                sb.append("Games:\n");
                for (Map.Entry<Integer, GameData> entry : gameMap.entrySet()) {
                    int index = entry.getKey();
                    GameData gameData = entry.getValue();
                    sb.append(index).append(". ");
                    sb.append("Game Name: ").append(gameData.gameName()).append(", ");
                    sb.append("White Username: ").append(gameData.whiteUsername() != null ? gameData.whiteUsername() : "Empty").append(", ");
                    sb.append("Black Username: ").append(gameData.blackUsername() != null ? gameData.blackUsername() : "Empty").append("\n");
                }

                return sb.toString();
            }
        } else {
            return "Failed to list games. HTTP error code: " + responseCode;
        }
    }

    public String joinGame(String... params) throws Exception {
        // Specify the endpoint
        URL fullUrl = new URL(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) fullUrl.openConnection();
        http.setRequestMethod("PUT");
        http.setRequestProperty("authorization", authToken);
        http.setDoOutput(true);

        // Handle join observer
        var playerColor = "";
        var observer = true;
        if (params.length == 2) {
            playerColor = params[1];
            observer = false;
        }

        // Handles gameID hiding from user
        currentGameID = Integer.parseInt(params[0]);
        GameData game = gameMap.get(currentGameID);
        String gameID = Integer.toString(game.gameID());

        // Write out the body
        var body = Map.of("gameID", gameID, "playerColor", playerColor);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make request
        int responseCode = http.getResponseCode();

        // Check if the request was successful
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Connect to WebSocket
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            if (observer) {
                ws.joinObserver(authToken, game.gameID(), username);
                state = UserState.OBSERVER;
            }
            else {
                ws.joinPlayer(authToken, game.gameID(), playerColor, username);
                state = UserState.PLAYER;
            }

            return "Join Game Success.";
        } else {
            return "Failed to Join Game or returned to previously joined game. HTTP code: " + responseCode;
        }
    }

    public UserState getState() {
        return this.state;
    }
}
