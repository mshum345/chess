package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String whiteUsername;
    public String blackUsername;
    public String[] observerNames;
    public Session session;

    public Connection(String visitorName, Session session) {
        this.whiteUsername = visitorName;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}