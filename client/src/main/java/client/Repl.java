package client;

import static ui.EscapeSequences.*;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Repl {
    private final ChessClient client;
    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.print(RESET_BG_COLOR);
        System.out.print(SET_TEXT_COLOR_GREEN);
        System.out.println("\uD83D\uDC36 Welcome to the Chess Client. Log in to start.");
        System.out.println(client.preHelp());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        var line = "";
        while (!result.equals("quit")) {
            printPrompt();
            line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.println(result);
            } catch (Throwable e) {
                System.out.println("Please check the required command parameters and try again. (Try typing help)");
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        if (client.getState()) {
            System.out.print("[LOGGED_IN] >>> ");
        }
        else {
            System.out.print("[LOGGED_OUT] >>> ");
        }
    }
}
