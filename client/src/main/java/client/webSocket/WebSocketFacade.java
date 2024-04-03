package client.webSocket;

import chess.*;
import client.VisualChessBoard;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class WebSocketFacade {
    private Session session;
    private VisualChessBoard boardPrinter;
    private ChessGame currentGame;
    private ChessGame.TeamColor userColor;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String serverUrl, NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
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
                        currentGame = serverMessage.getGame();
                        System.out.println(serverMessage.getMessage());
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        currentGame = serverMessage.getGame();

                        // Prints board
                        drawCurrentBoard();

                        System.out.println(serverMessage.getMessage());
                    } else {
                        System.out.println(serverMessage.getMessage());
                    }
                    notificationHandler.notify(serverMessage);
                }
            });

        } catch (Throwable ex) {
            ex.printStackTrace();
            System.out.println("Failure creating WebSocketFacade: " + ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameID, String playerColor, String username) {
        try {
            if (Objects.equals(playerColor, "BLACK")) {
                userColor = ChessGame.TeamColor.BLACK;
            }
            else {
                userColor = ChessGame.TeamColor.WHITE;
            }

            var command = new UserGameCommand(UserGameCommand.CommandType.JOIN_PLAYER, authToken, gameID, null, username, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure joining player: " + ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID, String username) {
        try {
            userColor = ChessGame.TeamColor.WHITE;
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

    public void makeMove(String authToken, int gameID, String[] params) {
        try {

            // get move
            var startPos = new ChessPosition(Integer.parseInt(params[0]), convertLetterToNum(params[1]));
            var endPos = new ChessPosition(Integer.parseInt(params[2]), convertLetterToNum(params[3]));
            ChessPiece.PieceType promoPiece = null;

            if (params.length > 4) {
                promoPiece = switch (params[4].toLowerCase()) {
                    case "pawn" -> ChessPiece.PieceType.PAWN;
                    case "rook" -> ChessPiece.PieceType.ROOK;
                    case "knight" -> ChessPiece.PieceType.KNIGHT;
                    case "bishop" -> ChessPiece.PieceType.BISHOP;
                    case "king" -> ChessPiece.PieceType.KING;
                    case "queen" -> ChessPiece.PieceType.QUEEN;
                    default -> null;
                };
            }

            var newMove = new ChessMove(startPos, endPos, promoPiece);

            // send move command
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, newMove, null, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Throwable ex) {
            System.out.println("Failure making move: " + ex.getMessage());
        }
    }

    public void drawCurrentBoard() {
        if (userColor == ChessGame.TeamColor.WHITE) {
            boardPrinter.printGivenBoardWhite(currentGame.getBoard(), null);
        } else {
            var tempBoard = new ChessBoard(currentGame.getBoard());
            tempBoard.flipBoard();
            boardPrinter.printGivenBoardBlack(tempBoard, null);
        }
    }

    public void drawLegalMoves(String row, String col) {
        var validMoves = currentGame.validMoves(new ChessPosition(Integer.parseInt(row), convertLetterToNum(col)));

        if (userColor == ChessGame.TeamColor.WHITE) {
            boardPrinter.printGivenBoardWhite(currentGame.getBoard(), validMoves);
        } else {
            // Flips board to black perspective
            var tempBoard = new ChessBoard(currentGame.getBoard());
            tempBoard.flipBoard();

            // Flips valid moves to black perspective
            var flippedMoves = new ArrayList<ChessMove>();
            for (ChessMove move : validMoves) {
                ChessPosition flippedStart = new ChessPosition(9 - move.getStartPosition().getRow(), 9 - move.getStartPosition().getColumn());
                ChessPosition flippedEnd = new ChessPosition(9 - move.getEndPosition().getRow(), 9 - move.getEndPosition().getColumn());
                flippedMoves.add(new ChessMove(flippedStart, flippedEnd, move.getPromotionPiece()));
            }

            // Prints the board
            boardPrinter.printGivenBoardBlack(tempBoard, validMoves);
        }
    }

    public int convertLetterToNum (String letter) {
        return switch (letter) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new IllegalArgumentException("Invalid letter: " + letter);
        };
    }
}
