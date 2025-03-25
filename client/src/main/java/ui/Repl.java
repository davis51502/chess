package ui;

import net.ClientCommunicator;
import net.ServerFacade;
import java.util.Scanner;

public class Repl implements ClientCommunicator {
    private static final String WELCOME_COLOR = "\u001B[38;2;255;102;204m";
    private static final String PROMPT_COLOR = "\u001B[38;2;34;139;34m";
    private static final String CHESS_CROWN = "\uD83D\uDC51";
    private static final String HEART_SYMBOL = "\u2661";

    private ClientObject client;
    private final ClientObject preClient;
    private final ClientObject postClient;
    private final ClientObject gameClient;
    private String authToken;
    private final ServerFacade serverFacade;

    public Repl(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);

        preClient = new PreLoginClient(serverUrl, this, serverFacade);
        postClient = new PostLoginClient(serverUrl, this, serverFacade);
        gameClient = new GameClient(serverUrl, this, serverFacade);

        client = preClient;
        authToken = null;
    }

    public void run() {
        displayWelcomeMessage();

        try (Scanner scanner = new Scanner(System.in)) {
            String result = "";
            while (!result.equals("quit")) {
                printPrompt();

                String line = scanner.nextLine();
                try {
                    result = processCommand(line);
                } catch (Throwable e) {
                    System.out.print(e.toString());
                }
            }
        }

        displayExitMessage();
    }

    private String processCommand(String line) throws Throwable {
        String result = client.eval(line);
        System.out.print(WELCOME_COLOR + result);

        if (client.getPost()) {
            this.authToken = serverFacade.getAuth();
            switchClient(postClient);
            client.connectAuthToken(authToken);
        } else if (client.getPre()) {
            switchClient(preClient);
        } else if (client.getGame()) {
            switchClient(gameClient);
        }

        return result;
    }

    private void displayWelcomeMessage() {
        System.out.print(WELCOME_COLOR);
        System.out.println(CHESS_CROWN + " Welcome to 240 chess. Type Help to get started. " + CHESS_CROWN);
        System.out.print(client.help());
    }

    private void displayExitMessage() {
        System.out.println();
        System.out.println("Thanks for playing " + HEART_SYMBOL);
    }

    public void switchClient(ClientObject newClient) {
        client = newClient;
    }

    private void printPrompt() {
        System.out.print("\n>>> " + PROMPT_COLOR);
    }
}