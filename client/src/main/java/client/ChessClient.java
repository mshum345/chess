package client;

import com.google.gson.Gson;
import model.ResponseData;
import model.GameData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessClient {
    private final String serverUrl;
    private boolean loggedIn = false;
    private String authToken;
    Map<Integer, GameData> gameMap;
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

    public String eval(String input) throws Exception {
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
        if (params.length == 2) {
            playerColor = params[1];
        }

        // Handles gameID hiding from user
        GameData game = gameMap.get(Integer.parseInt(params[0]));
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
            printBoardWhite();
            printBoardBlack();
            return "Join Game Success.";
        } else {
            return "Failed to Join Game. HTTP error code: " + responseCode;
        }
    }

    public boolean getState() {
        return loggedIn;
    }

    public void printBoardWhite() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"a", "b", "c", "d", "e", "f", "g", "h"};

        drawGreyRow(out, symbols);
        drawRowSpecialPieces(out, "RED", 1, "8");
        drawRowPawns(out, "RED", 2, "7");
        drawEmptyRow(out, 1, "6");
        drawEmptyRow(out, 2, "5");
        drawEmptyRow(out, 1, "4");
        drawEmptyRow(out, 2, "3");
        drawRowPawns(out, "BLUE", 1, "2");
        drawRowSpecialPieces(out, "BLUE", 2, "1");
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public void printBoardBlack() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"h", "g", "f", "e", "d", "c", "b", "a"};

        drawGreyRow(out, symbols);
        drawRowSpecialPieces(out, "BLUE", 1, "1");
        drawRowPawns(out, "BLUE", 2, "2");
        drawEmptyRow(out, 1, "3");
        drawEmptyRow(out, 2, "4");
        drawEmptyRow(out, 1, "5");
        drawEmptyRow(out, 2, "6");
        drawRowPawns(out, "RED", 1, "7");
        drawRowSpecialPieces(out, "RED", 2, "8");
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void drawRowPawns(PrintStream out, String pieceColor, int modNum, String greySymbol) {
        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printPieceOnBlack(out, "P", pieceColor);
            }
            else {
                printPieceOnWhite(out, "P", pieceColor);
            }
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawRowSpecialPieces(PrintStream out, String pieceColor, int modNum, String greySymbol) {
        String[] pieces = {"R", "N", "B", "K", "Q", "B", "N", "R"};
        var j = 0;

        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printPieceOnBlack(out, pieces[j], pieceColor);
            }
            else {
                printPieceOnWhite(out, pieces[j], pieceColor);
            }
            j++;
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawEmptyRow(PrintStream out, int modNum, String greySymbol) {
        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printEmptyBlack(out);
            }
            else {
                printEmptyWhite(out);
            }
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawGreyRow(PrintStream out, String[] symbols) {
        printEmptyGrey(out);
        for (int i = 0; i < 8; i++) {
            printCharOnGrey(out, symbols[i]);
        }
        printEmptyGrey(out);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printPieceOnBlack(PrintStream out, String piece, String pieceColor) {
        if (pieceColor == "RED") {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_RED);
            out.print(" " + piece + " ");
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(" " + piece + " ");
        }
    }

    private static void printPieceOnWhite(PrintStream out, String piece, String pieceColor) {
        if (pieceColor == "RED") {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_RED);
            out.print(" " + piece + " ");
        }
        else {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(" " + piece + " ");
        }
    }

    private static void printCharOnGrey(PrintStream out, String symbol) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + symbol + " ");
    }


    private static void printEmptyGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("   ");
    }

    private static void printEmptyWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print("   ");
    }

    private static void printEmptyBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print("   ");
    }
}
