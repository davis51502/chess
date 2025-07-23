package client;

import client.handlers.Gameplay;
import client.handlers.PostLogin;
import client.handlers.PreLogin;
import client.ui.BoardGenerator;

// this is the conductor
public class ChessClient {

    private final ServerFacade serverFacade;
    private final ClientState clientState= new ClientState();
    private final BoardGenerator boardGenerator = new BoardGenerator();

    public ChessClient(String serverURL) {
        this.serverFacade = new ServerFacade(serverURL);
    }
    public void run() {
        System.out.println("welcome to chess, type 'help' to get started");
         new Repl(this).run();
    }
    public void eval(String... args) {
        try {
            if (!clientState.isLoggedIn()) {
                new PreLogin(serverFacade, clientState).handle(args);
            } else if (!clientState.isInGame()) {
                new PostLogin(serverFacade, clientState, boardGenerator).handle(args);
            } else {
                new Gameplay(clientState, boardGenerator).handle(args);
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
    public String getPrompt() {
        return clientState.getPrompted();
    }
}
