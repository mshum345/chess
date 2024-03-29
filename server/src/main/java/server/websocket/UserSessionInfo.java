package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public class UserSessionInfo {
    private String username;
    private String playerColor;
    private Session session;

    public UserSessionInfo(String username, String playerColor, Session session) {
        this.username = username;
        this.playerColor = playerColor;
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public Session getSession() {
        return session;
    }
}
