package client;

import java.util.Arrays;

public class ChessClient {
    private final String serverUrl;
    private boolean state = false;
    public ChessClient(String url) {
        this.serverUrl = url;
    }

    public String preHelp() {
        return "PreHELP";
    }

    public String postHelp() {
        return "PostHELP";
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

    public String register(String... params) throws Exception {
        if (params.length >= 1) {
            state = true;
            var visitorName = String.join("-", params);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception("Expected: <yourname>");
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
