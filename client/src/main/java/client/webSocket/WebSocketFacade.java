package client.webSocket;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserSessionCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.Map;

public class WebSocketFacade {
    private Session session;

    public WebSocketFacade(String serverUrl) {
        try {
            serverUrl = serverUrl.replace("http", "ws");
            URI socketURI = new URI(serverUrl + "/connect");

            // Creates session
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    System.out.println(serverMessage.getMessage());
                }
            });

        } catch (Throwable ex) {
            System.out.println("Failure creating WebSocketFacade: " + ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameID, String playerColor, String username) {
        try {
            var command = new UserSessionCommand(authToken, gameID, playerColor, UserGameCommand.CommandType.JOIN_PLAYER, username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure joining player: " + ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID, String username) {
        try {
            var command = new UserSessionCommand(authToken, gameID, null, UserGameCommand.CommandType.JOIN_OBSERVER, username);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure joining observer: " + ex.getMessage());
        }
    }
}
