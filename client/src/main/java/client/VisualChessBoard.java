package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class VisualChessBoard {

    public void printBoardWhite() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"a", "b", "c", "d", "e", "f", "g", "h"};

        drawGreyRow(out, symbols);
        drawRowSpecialPieces(out, "RED", 1, "8");
        drawRowPawns(out, "RED", 2, "7");
        drawEmptyRow(out, 1, "6");
        drawEmptyRow(out, 2, "5");
        drawEmptyRow(out, 1, "4");
        drawEmptyRow(out, 2, "3");
        drawRowPawns(out, "BLUE", 1, "2");
        drawRowSpecialPieces(out, "BLUE", 2, "1");
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    public void printBoardBlack() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var symbols = new String[] {"h", "g", "f", "e", "d", "c", "b", "a"};

        drawGreyRow(out, symbols);
        drawRowSpecialPieces(out, "BLUE", 1, "1");
        drawRowPawns(out, "BLUE", 2, "2");
        drawEmptyRow(out, 1, "3");
        drawEmptyRow(out, 2, "4");
        drawEmptyRow(out, 1, "5");
        drawEmptyRow(out, 2, "6");
        drawRowPawns(out, "RED", 1, "7");
        drawRowSpecialPieces(out, "RED", 2, "8");
        drawGreyRow(out, symbols);
        out.print(SET_TEXT_COLOR_GREEN);
    }

    private static void drawRowPawns(PrintStream out, String pieceColor, int modNum, String greySymbol) {
        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printPieceOnBlack(out, "P", pieceColor);
            }
            else {
                printPieceOnWhite(out, "P", pieceColor);
            }
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawRowSpecialPieces(PrintStream out, String pieceColor, int modNum, String greySymbol) {
        String[] pieces = {"R", "N", "B", "K", "Q", "B", "N", "R"};
        var j = 0;

        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printPieceOnBlack(out, pieces[j], pieceColor);
            }
            else {
                printPieceOnWhite(out, pieces[j], pieceColor);
            }
            j++;
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void drawEmptyRow(PrintStream out, int modNum, String greySymbol) {
        printCharOnGrey(out, greySymbol);
        for (int i = modNum; i < modNum + 8; i++) {
            if (i % 2 == 1) {
                printEmptyBlack(out);
            }
            else {
                printEmptyWhite(out);
            }
        }
        printCharOnGrey(out, greySymbol);

        out.print(RESET_BG_COLOR);
        out.println();
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

    private static void printPieceOnBlack(PrintStream out, String piece, String pieceColor) {
        if (pieceColor == "RED") {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_RED);
            out.print(" " + piece + " ");
        }
        else {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(" " + piece + " ");
        }
    }

    private static void printPieceOnWhite(PrintStream out, String piece, String pieceColor) {
        if (pieceColor == "RED") {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_RED);
            out.print(" " + piece + " ");
        }
        else {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(" " + piece + " ");
        }
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

    private static void printEmptyWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print("   ");
    }

    private static void printEmptyBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print("   ");
    }
}
