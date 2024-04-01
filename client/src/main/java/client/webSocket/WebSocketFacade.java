package client.webSocket;

import chess.ChessMove;
import client.VisualChessBoard;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade {
    private Session session;
    private VisualChessBoard boardPrinter;

    public WebSocketFacade(String serverUrl) {
        boardPrinter = new VisualChessBoard();

        try {
            serverUrl = serverUrl.replace("http", "ws");
            URI socketURI = new URI(serverUrl + "/connect");

            // Creates session
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig endpointConfig) {}
            }, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        System.out.println(serverMessage.getMessage());
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        boardPrinter.printGivenBoard(serverMessage.getBoard());
                        System.out.println(serverMessage.getMessage());
                    } else {
                        System.out.println(serverMessage.getMessage());
                    }
                }
            });

        } catch (Throwable ex) {
            ex.printStackTrace();
            System.out.println("Failure creating WebSocketFacade: " + ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameID, String playerColor, String username) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.JOIN_PLAYER, authToken, gameID, null, username, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure joining player: " + ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID, String username) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.JOIN_OBSERVER, authToken, gameID, null, username, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure joining observer: " + ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID, null, null, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure resigning from game: " + ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, null, null, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure leaving game: " + ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove newMove) {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, newMove, null, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure making move: " + ex.getMessage());
        }
    }
}
