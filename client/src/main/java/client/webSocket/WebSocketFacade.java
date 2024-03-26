package client.webSocket;

import java.util.Map;

public class WebSocketFacade {
    private String serverUrl;

    public WebSocketFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void joinPlayer(String authToken, Map<String, String> body, String username) {
    }

    public void joinObserver(String authToken, Map<String, String> body, String username) {
    }
}
