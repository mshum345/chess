package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(action.getUsername(), session);
            case JOIN_OBSERVER -> joinObserver(action.getUsername(), session);
            // more cases
        }
    }

    private void joinPlayer(String username, Session session) throws Exception {
        connections.add(username, session);
        var message = String.format("%s has joined the game as player", username);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(username, serverMessage);
    }

    private void joinObserver(String username, Session session) throws Exception {
        connections.add(username, session);
        var message = String.format("%s has joined the game as observer", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(username, notification);
    }
}
