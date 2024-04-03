package client.webSocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
    void notify(ServerMessage serverMessage);
}