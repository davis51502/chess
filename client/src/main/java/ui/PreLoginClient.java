package ui;
import chess.InvalidMoveException;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;
import java.util.Arrays;

public class PreLoginClient implements ClientObject {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;
    private String authToken;
    private boolean isPreLoginStage;
    private boolean isPostLoginStage;
    private boolean isInGameStage;

    public PreLoginClient(String serverUrl, ClientCommunicator notificationHandler, ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        resetStages();
    }
    public void connectAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public String help() {
        return """
            Available Commands:
            register <USERNAME> <PASSWORD> <EMAIL> - Create a new account
            login <USERNAME> <PASSWORD> - Log in to an existing account
            quit - Exit the chess application
            help - Display this help menu
            """;
    }

    public String eval(String input) {
        // Reset stages before processing
        resetStages();

        try {
            String[] tokens = input.split(" ");
            String cmd = tokens.length > 0 ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unknown command. Type 'help' for available commands.";
            };
        } catch (InvalidMoveException ex) {
            return ex.getMessage();
        }
    }
    public String register(String... params) throws InvalidMoveException {
        if (params.length == 3) {
            serverFacade.register(new RegisterRequest(params[0], params[1], params[2]));
            isPostLoginStage = true;
            return String.format("Successfully registered as %s.", params[0]);
        }
        throw new InvalidMoveException(400, "Registration requires: <USERNAME> <PASSWORD> <EMAIL>");
    }
    public String login(String... params) throws InvalidMoveException {
        if (params.length == 2) {
            serverFacade.login(new LoginRequest(params[0], params[1]));
            isPostLoginStage = true;
            return String.format("Successfully logged in as %s.", params[0]);
        }
        throw new InvalidMoveException(400, "Registration requires: <USERNAME> <PASSWORD> <EMAIL>");
    }

    /**
     * Resets all stage flags to their initial state.
     */
    private void resetStages() {
        isPreLoginStage = false;
        isPostLoginStage = false;
        isInGameStage = false;
    }

    // Getter methods for stage states
    public boolean getPost() { return isPostLoginStage; }
    public boolean getPre() { return isPreLoginStage; }
    public boolean getGame() { return isInGameStage; }
}
