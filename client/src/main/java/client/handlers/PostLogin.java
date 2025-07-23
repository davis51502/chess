package client.handlers;

import client.ClientState;
import client.ServerFacade;
import client.ui.BoardGenerator;
import model.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// create list join and logout
public class PostLogin {

    private final ServerFacade serverFacade;
    private final ClientState state ;
    private final BoardGenerator boardGenerator;
    private final Scanner scanner = new Scanner(System.in);

    public PostLogin(ServerFacade serverFacade, ClientState state, BoardGenerator boardGenerator) {
        this.serverFacade = serverFacade;
        this.state = state;
        this.boardGenerator = boardGenerator;
    }
    public void handle(String... args) throws Exception {
        String command = args[0].toLowerCase();
        String[] params = Arrays.copyOfRange(args, 1, args.length);
        switch (command) {
            case "create" -> create();
            case "list" -> list();
            case "play" -> join(false);
            case "observe" -> join(true);
            case "logout" -> logout();
            case "help" -> help();
            default -> System.out.println("invalid command, type 'help' for command options");
        }
    }
    public void create() throws Exception {
        System.out.print("game name: ");
        String gameName = scanner.nextLine();
        serverFacade.createGame(state.getAuthToken(), gameName);
        System.out.println("game `" + gameName + "` created.");
    }
    public void list() throws Exception {
        var games = serverFacade.listGames(state.getAuthToken());
        state.setGamesList(new ArrayList<>(games));
        if (state.getGamesList().isEmpty()) {
            System.out.println("no games available");
            return;
        }
        for (int i =0; i< state.getGamesList().size(); i++ ) {
            GameData data = state.getGamesList().get(i);
            System.out.printf("%d. %s (white: %s, black: %s) %n ",
                    i +1 , data.gameName(), data.whiteUsername(), data.blackUsername());
        }
    }
    private void help() throws Exception {
        System.out.println("""
                Available commands: 
                create = create anew game 
                list - list all games 
                play = join game as player  
                observe = join game as observer 
                logout = sign out of your account 
                help - show this message again 
                """);
    }
}
