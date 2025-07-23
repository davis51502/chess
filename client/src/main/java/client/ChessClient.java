package client;

import client.handlers.Gameplay;
import client.handlers.PostLogin;
import client.handlers.PreLogin;

// this is the conductor
public class ChessClient {

    private final String serverURL;
    private final ServerFacade serverFacade;
    private final ClientState clientState= new ClientState();

    public ChessClient(String serverURL, ServerFacade serverFacade) {
        this.serverURL = serverURL;
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
//                new PostLogin()
            } else {
//                new Gameplay()
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
    public String getPrompt() {
        return clientState.getPrompted();
    }
}
