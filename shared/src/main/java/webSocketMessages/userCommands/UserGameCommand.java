package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {
    private final Integer gameID;
    private final ChessMove move;
    private final String username;
    protected CommandType commandType;
    private final String authToken;
    private final String playerColor;

    public UserGameCommand(CommandType type, String authToken, int gameID, ChessMove move, String username, String playerColor) {
        this.authToken = authToken;
        this.commandType = type;
        this.gameID = gameID;
        this.move = move;
        this.username = username;
        this.playerColor = playerColor;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
