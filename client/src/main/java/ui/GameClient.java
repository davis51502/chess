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

    /**
     * Constructs a GameClient with the specified server configuration.
     *
     * @param serverUrl URL of the server
     * @param notificationHandler Handler for client communications
     * @param serverFacade Facade for server interactions
     */
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

    /**
     * Connects the client with an authentication token.
     *
     * @param authToken Authentication token for the session
     */
    public void connectAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Resets all state flags to their default (false) state.
     */
    private void resetStateFlags() {
        this.pre = false;
        this.post = false;
        this.game = false;
    }

    /**
     * Provides help information for the current client state.
     *
     * @return Help text (currently empty)
     */
    public String help() {
        return "";
    }

    /**
     * Evaluates the input command for the current client state.
     *
     * @param line Input command to evaluate
     * @return Result of command evaluation (currently empty)
     */
    public String eval(String line) {
        return "";
    }

    /**
     * Checks if the client is in post-login state.
     *
     * @return true if in post-login state, false otherwise
     */
    public boolean getPost() {
        return post;
    }

    /**
     * Checks if the client is in pre-login state.
     *
     * @return true if in pre-login state, false otherwise
     */
    public boolean getPre() {
        return pre;
    }

    /**
     * Checks if the client is in game state.
     *
     * @return true if in game state, false otherwise
     */
    public boolean getGame() {
        return game;
    }
}