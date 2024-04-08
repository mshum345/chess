package client;
import client.webSocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;
import static ui.EscapeSequences.*;
import java.util.Scanner;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
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
        if (client.getState() == ChessClient.UserState.LOGGED_IN) {
            System.out.print("[LOGGED_IN] >>> ");
        }
        else if (client.getState() == ChessClient.UserState.PLAYER){
            System.out.print("[PLAYER] >>> ");
        }
        else if (client.getState() == ChessClient.UserState.OBSERVER){
            System.out.print("[OBSERVER] >>> ");
        }
        else {
            System.out.print("[LOGGED_OUT] >>> ");
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        printPrompt();
    }
}
