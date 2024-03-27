package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.HashMap;

public class WebSocketHandler {

    // Map of GameID to a map of authtokens to sessions
    private final HashMap<Integer, HashMap<String, Session>> gameSessions = new HashMap<Integer, HashMap<String, Session>>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message, String username) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(command, session, username);
            case JOIN_OBSERVER -> joinObserver(command, session);
            // more cases
        }
    }

    private void joinPlayer(UserGameCommand command, Session session, String username) throws Exception {
        // Inserts session into gameSessions HashMap
        HashMap<String, Session> newHashMap = new HashMap<>();
        newHashMap.put(command.getAuthToken(), session);
        gameSessions.put(command.getGameID(), newHashMap);

        // Broadcasts message to all sessions but player who just joined
        var message = String.format("%s has joined the game as player", username);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        broadcast();
    }

    private void joinObserver(UserGameCommand command, Session session) throws Exception {

    }

    private void broadcast() {

    }
}
