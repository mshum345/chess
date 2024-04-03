package client;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class VisualChessBoard {

    public void printGivenBoardWhite(ChessBoard board, Collection<ChessMove> validMoves) {
        System.out.println();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"a", "b", "c", "d", "e", "f", "g", "h"};
        int k = 1;
        int greySymbol = 8;
        boolean isValidMove = false;

        drawGreyRow(out, symbols);
        for (int i = 1; i < 9; i++) {
            drawGreySymbol(out, greySymbol);
            for (int j = 1; j < 9; j++) {
                // Determines if it is a valid move
                if (validMoves != null) {
                    for (ChessMove move: validMoves) {
                        if (Objects.equals(move.getEndPosition(), new ChessPosition(i, j))) {
                            isValidMove = true;
                            break;
                        }
                    }
                }

                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if (k % 2 == 1) {
                    drawPiece(out, piece, "WHITE", isValidMove);
                }
                else {
                    drawPiece(out, board.getPiece(new ChessPosition(i,j)), "BLACK", isValidMove);
                }
                k++;
                isValidMove = false;
            }
            drawGreySymbol(out, greySymbol);
            out.print(SET_BG_COLOR_BLACK);
            out.println();
            k++;
            greySymbol--;
        }
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public void printGivenBoardBlack(ChessBoard board, Collection<ChessMove> validMoves) {
        System.out.println();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"h", "g", "f", "e", "d", "c", "b", "a"};
        int k = 2;
        int greySymbol = 1;
        boolean isValidMove = false;

        // FLIP ALL VALIDMOVES TO BLACK PERSPECTIVE

        drawGreyRow(out, symbols);
        for (int i = 1; i < 9; i++) {
            drawGreySymbol(out, greySymbol);
            for (int j = 1; j < 9; j++) {
                // Determines if it is a valid move
                if (validMoves != null) {
                    for (ChessMove move: validMoves) {
                        if (Objects.equals(move.getEndPosition(), new ChessPosition(i, j))) {
                            isValidMove = true;
                            break;
                        }
                    }
                }

                if (k % 2 == 1) {
                    drawPiece(out, board.getPiece(new ChessPosition(i,j)), "WHITE", isValidMove);
                }
                else {
                    drawPiece(out, board.getPiece(new ChessPosition(i,j)), "BLACK", isValidMove);
                }
                k++;
                isValidMove = false;
            }
            drawGreySymbol(out, greySymbol);
            out.print(SET_BG_COLOR_BLACK);
            out.println();
            greySymbol++;
        }
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private void drawPiece(PrintStream out, ChessPiece piece, String bgColor, boolean isValidMove) {
        if (piece == null) {
            if (isValidMove) {
                if (Objects.equals(bgColor, "WHITE")) {
                    out.print(SET_BG_COLOR_GREEN);
                    out.print(SET_TEXT_COLOR_RED);
                    out.print("   ");
                } else {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                    out.print(SET_TEXT_COLOR_RED);
                    out.print("   ");
                }
            }
            else {
                if (Objects.equals(bgColor, "WHITE")) {
                    out.print(SET_BG_COLOR_WHITE);
                    out.print(SET_TEXT_COLOR_RED);
                    out.print("   ");
                } else {
                    out.print(SET_BG_COLOR_BLACK);
                    out.print(SET_TEXT_COLOR_RED);
                    out.print("   ");
                }
            }
        }
        else {
            var symbol = switch (piece.getPieceType()) {
                case PAWN -> "P";
                case KNIGHT -> "N";
                case ROOK -> "R";
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
            };

            if (isValidMove) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (Objects.equals(bgColor, "WHITE")) {
                        out.print(SET_BG_COLOR_GREEN);
                        out.print(SET_TEXT_COLOR_RED);
                        out.print(" " + symbol + " ");
                    } else {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                        out.print(SET_TEXT_COLOR_RED);
                        out.print(" " + symbol + " ");
                    }
                }
                else {
                    if (Objects.equals(bgColor, "WHITE")) {
                        out.print(SET_BG_COLOR_GREEN);
                        out.print(SET_TEXT_COLOR_BLUE);
                        out.print(" " + symbol + " ");
                    } else {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                        out.print(SET_TEXT_COLOR_BLUE);
                        out.print(" " + symbol + " ");
                    }
                }
            } else {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (Objects.equals(bgColor, "WHITE")) {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(SET_TEXT_COLOR_RED);
                        out.print(" " + symbol + " ");
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(SET_TEXT_COLOR_RED);
                        out.print(" " + symbol + " ");
                    }
                }
                else {
                    if (Objects.equals(bgColor, "WHITE")) {
                        out.print(SET_BG_COLOR_WHITE);
                        out.print(SET_TEXT_COLOR_BLUE);
                        out.print(" " + symbol + " ");
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                        out.print(SET_TEXT_COLOR_BLUE);
                        out.print(" " + symbol + " ");
                    }
                }
            }
        }
    }

    private void drawGreySymbol(PrintStream out, int symbol) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + symbol + " ");
    }

    private static void drawGreyRow(PrintStream out, String[] symbols) {
        printEmptyGrey(out);
        for (int i = 0; i < 8; i++) {
            printCharOnGrey(out, symbols[i]);
        }
        printEmptyGrey(out);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printCharOnGrey(PrintStream out, String symbol) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.print(" " + symbol + " ");
    }

    private static void printEmptyGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print("   ");
    }
}
