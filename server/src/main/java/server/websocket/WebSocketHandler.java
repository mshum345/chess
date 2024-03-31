package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.HashMap;

public class WebSocketHandler {

    // Map of GameID to a map of authtokens to sessions
    private final HashMap<Integer, HashMap<String, UserSessionInfo>> gameSessions;

    public WebSocketHandler() {
        gameSessions = new HashMap<Integer, HashMap<String, UserSessionInfo>>();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER, JOIN_OBSERVER -> joinUser(command, session);
            case RESIGN -> resign(command, session);
            case LEAVE -> leave(command, session);
            case MAKE_MOVE -> makeMove(command, session);
        }
    }

    private void makeMove(UserGameCommand command, Session session) {

    }

    private void leave(UserGameCommand command, Session session) {
        // Send Server Message
        var message = String.format("%s has left the game", command.getUsername());
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(serverMessage, command.getGameID(), command.getAuthToken());

        // Remove userInfo from gameSessions
        gameSessions.get(command.getGameID()).remove(command.getAuthToken());
    }

    private void resign(UserGameCommand command, Session session) {
        // Send Server Message
        var message = String.format("%s has resigned from the game", command.getUsername());
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(serverMessage, command.getGameID(), command.getAuthToken());

        // Remove game from gameSessions
        gameSessions.remove(command.getGameID());
    }

    private void joinUser(UserGameCommand command, Session session) throws Exception {
        // Inserts UserSessionInfo into gameSessions HashMap
        var newUserInfo = new UserSessionInfo(command.getUsername(), command.getPlayerColor(), session);
        HashMap<String, UserSessionInfo> newHashMap = new HashMap<>();
        newHashMap.put(command.getAuthToken(), newUserInfo);
        gameSessions.put(command.getGameID(), newHashMap);

        // sets correct notification message (Observer or Player)
        var message = "";
        if (command.getPlayerColor().equals(null)) {
            // Broadcasts to all that an observer has joined
            message = String.format("%s has joined the game as an observer", command.getUsername());
        }
        else {
            // Broadcasts to all that a player has joined
            message = String.format("%s has joined the game as the %s player", command.getUsername(), command.getPlayerColor());
        }

        // Broadcasts message to all sessions but player who just joined
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(serverMessage, command.getGameID(), command.getAuthToken());

    }

    private void broadcast(ServerMessage serverMessage, int gameID, String excludeAuth) {
        HashMap<String, UserSessionInfo> sessionMap = gameSessions.get(gameID);

        for (HashMap.Entry<String, UserSessionInfo> entry : sessionMap.entrySet()) {
            String auth = entry.getKey();
            if (!auth.equals(excludeAuth)) {
                UserSessionInfo userInfo = entry.getValue();
                try {
                    userInfo.getSession().getRemote().sendString(new Gson().toJson(serverMessage));
                } catch (Throwable e) {
                    System.out.println("Error broadcasting serverMessage: " + e.getMessage());
                }
            }
        }
    }
}
