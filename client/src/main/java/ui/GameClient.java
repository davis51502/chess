package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;

public class GameClient implements ClientObject {
    // Client State Fields
    private String gameID;
    private String authToken;

    // Server Communication Fields
    private final ServerFacade server;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;

    // State Flags
    private boolean pre;
    private boolean post;
    private boolean game;


    public GameClient(
            String serverUrl,
            ClientCommunicator notificationHandler,
            ServerFacade serverFacade
    ) {
        this.server = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;

        // Initialize all state flags to false
        resetStateFlags();
    }


    public void connectAuthToken(String authToken) {
        this.authToken = authToken;
    }


    private void resetStateFlags() {
        this.pre = false;
        this.post = false;
        this.game = false;
    }


    public String help() {
        return "";
    }


    public String eval(String line) {
        return "";
    }


    public boolean getPost() {
        return post;
    }


    public boolean getPre() {
        return pre;
    }


    public boolean getGame() {
        return game;
    }
}