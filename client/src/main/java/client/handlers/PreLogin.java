package client.handlers;

import client.ClientState;
import client.ServerFacade;

import java.util.Scanner;

// login register and help
public class PreLogin {
    private final ServerFacade serverFacade;
    private final ClientState state;
    private final Scanner scanner = new Scanner(System.in);

    public PreLogin(ServerFacade serverFacade, ClientState state) {
        this.serverFacade = serverFacade;
        this.state = state;
    }
    public void handle(String... args) throws Exception {
    String command = args[0].toLowerCase();
    switch (command) {
        case "register" -> register();
        case "login" -> login();
        case "help" -> help();
        case "quit" -> {System.out.println("BYE! BYE!"); System.exit(0);}
        default -> System.out.println("invalid command, type 'help' for command options");
    }

    }
    private void register() throws Exception {
        System.out.print("username: " );
        String username = scanner.nextLine();
        System.out.print("password: " );
        String password = scanner.nextLine();
        System.out.print("email: " );
        String email = scanner.nextLine();

    }
}
