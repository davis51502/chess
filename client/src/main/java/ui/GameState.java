package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.*;

import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

enum State {
    LOGGED_OUT,
    LOGGED_IN,
    IN_GAME
}

public class GameState {
    private State currentState;
    private final Scanner scanner = new Scanner(System.in);
    private ServerFacade serverFacade;
    private final DrawChessBoard boardDrawer = new DrawChessBoard();
    private List<GameData> gameList;

    public GameState() {
        currentState = State.LOGGED_OUT;
        serverFacade = new ServerFacade(null, 0);
    }

    public void getCommand() {
        System.out.print(SET_TEXT_COLOR_WHITE);
        switch (currentState) {
            case LOGGED_OUT:
                System.out.print("[LOGGED_OUT] >>> ");
                break;
            case LOGGED_IN:
                System.out.print("[LOGGED_IN] >>> ");
                break;
            case IN_GAME:
                System.out.print("[IN_GAME] >>> ");
                break;
        }
        String line = scanner.nextLine();
        var command = line.split(" ");
        switch (currentState) {
            case LOGGED_OUT:
                loggedOutCommands(command);
                break;
            case LOGGED_IN:
                loggedInCommands(command);
                break;
            case IN_GAME:
                inGameCommands(command);
                break;
        }
    }

    private void loggedOutCommands(String[] command) {
        switch (command[0].toLowerCase()) {
            case "help":
                displayLoggedOutHelp();
                break;
            case "quit":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            case "register":
                tryRegister(command);
                break;
            case "login":
                tryLogin(command);
                break;
            case "clear":
                attemptDatabaseClear();
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private void displayLoggedOutHelp() {
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Help\t\t\t\t\t\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- List all available commands\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Quit\t\t\t\t\t\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Quit the chess client\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Login\t\t");
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("<USERNAME> <PASSWORD>\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Login to the server\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Register\t");
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("<USERNAME> <PASSWORD> <EMAIL>\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Create an account\n");
    }

    private void tryRegister(String[] command) {
        try {
            if (command.length != 4) {
                throw new IllegalArgumentException("Invalid number of arguments. Expected 4 arguments.");
            }
            UserData newUser = new UserData(command[1], command[2], command[3]);
            serverFacade.register(newUser);
            printSuccessfulLogin(command[1]);
            currentState = State.LOGGED_IN;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void tryLogin(String[] command) {
        try {
            if (command.length != 3) {
                throw new IllegalArgumentException("Invalid number of arguments. Expected 3 arguments.");
            }
            LoginRequest loginInfo = new LoginRequest(command[1], command[2]);
            serverFacade.login(loginInfo);
            printSuccessfulLogin(command[1]);
            currentState = State.LOGGED_IN;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void printSuccessfulLogin(String username) {
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("Logged in as: ");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print(username);
        System.out.println();
    }

    private void attemptDatabaseClear() {
        try {
            if (serverFacade.clearDatabase()) {
                System.out.println("Database cleared");
            } else {
                System.out.println("Failed to clear database");
            }
        } catch (Exception e) {
            System.out.println("Error clearing database");
        }
    }

    private void loggedInCommands(String[] command) {
        switch (command[0].toLowerCase()) {
            case "help":
                printLoggedInHelp();
                break;
            case "logout":
                performLogout();
                break;
            case "create":
                createGameCommand(command);
                break;
            case "list":
                listGamesCommand();
                break;
            case "join":
                try {
                    joinGameCommand(command);
                } catch (Exception e) {
                    System.out.print(SET_TEXT_COLOR_RED);
                    System.out.println(e.getMessage());
                    System.out.println(RESET_TEXT_COLOR);
                }
                break;
            case "observe":
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                boardDrawer.drawBoard(board, ChessGame.TeamColor.WHITE);
                break;
            case "quit":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    private void performLogout() {
        try {
            serverFacade.logout();
            System.out.print(SET_TEXT_COLOR_CYAN);
            System.out.print("Logged out");
            System.out.println();
            currentState = State.LOGGED_OUT;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createGameCommand(String[] command) {
        try {
            if (command.length != 2) {
                throw new IllegalArgumentException("Invalid number of arguments. Expected 2 arguments.");
            }
            serverFacade.createGame(new CreateGameRequest(command[1]));
            System.out.print(SET_TEXT_COLOR_CYAN);
            System.out.print("Created game \"");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
            System.out.print(command[1]);
            System.out.print(SET_TEXT_COLOR_CYAN);
            System.out.print("\"");
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void listGamesCommand() {
        try {
            gameList = serverFacade.listGames();
            printGameList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printLoggedInHelp() {
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Help\t\t\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- List all available commands\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Quit\t\t\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Quit the chess client\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("List\t\t\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- List all available games\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Create\t");
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("<NAME>\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Create a new game\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Join\t");
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("<ID> [WHITE|BLACK]\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Join a game\n");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Observe\t");
        System.out.print(SET_TEXT_COLOR_CYAN);
        System.out.print("<ID>\t\t\t\t\t");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
        System.out.print("- Observe a game\n");
    }

    private void printGameList() {
        System.out.print(RESET_TEXT_COLOR);
        System.out.println("ID Game Name           White Player        Black Player");
        int index = 1;
        for (GameData game : gameList) {
            String whiteUsername = (game.getWhiteUsername() != null) ? game.getWhiteUsername() : "---";
            String blackUsername = (game.getBlackUsername() != null) ? game.getBlackUsername() : "---";
            System.out.print(SET_TEXT_COLOR_CYAN);
            System.out.print(String.format("%-3d", index));
            System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREEN);
            System.out.print(String.format("%-20s", game.getGameName()));
            System.out.print(SET_TEXT_COLOR_PURPLE);
            System.out.print(String.format("%-20s", whiteUsername));
            System.out.print(String.format("%-20s", blackUsername));
            System.out.println();
            index++;
        }
    }

    private void joinGameCommand(String[] command) throws Exception {
        if (gameList == null) {
            throw new IllegalArgumentException("Please list games before joining any");
        }
        if (command.length != 3) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected 3 arguments.");
        }

        int gameNumber = Integer.parseInt(command[1]);

        // Check arguments
        if (!(gameNumber >= 1 && gameNumber <= gameList.size())) {
            throw new IllegalArgumentException("Invalid game ID");
        }
        if (!(command[2].equalsIgnoreCase("WHITE") || command[2].equalsIgnoreCase("BLACK"))) {
            throw new IllegalArgumentException("Invalid color");
        }

        GameData game = gameList.get(gameNumber - 1);
        int gameID = game.getGameID();

        ChessGame.TeamColor teamColor = command[2].equalsIgnoreCase("WHITE")
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;
        if (
                (teamColor == ChessGame.TeamColor.WHITE
                        && game.getWhiteUsername() != null)
                        || (teamColor == ChessGame.TeamColor.BLACK
                        && game.getBlackUsername() != null)
        ) {
            throw new IllegalArgumentException("Color already taken");
        } else {
            serverFacade.joinGame(new GameJoinRequest(command[2].toUpperCase(), gameID));
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            boardDrawer.drawBoard(board, teamColor);
        }
    }

    private void inGameCommands(String[] command) {
        switch (command[0].toLowerCase()) {
            case "leave":
                System.out.println("Leaving game");
                currentState = State.LOGGED_IN;
                break;
            default:
                System.out.println("Invalid command");
        }
    }
}