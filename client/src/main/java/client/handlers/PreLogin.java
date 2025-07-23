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
        var auth  = serverFacade.register(username,password,email);
        state.setAuthToken(auth.authToken());
        state.setUsername(auth.username());
        System.out.println("registered + logged in as " + auth.username());
    }
    private void login() throws Exception {
        System.out.print("username: " );
        String username = scanner.nextLine();
        System.out.print("password: " );
        String password = scanner.nextLine();
        var auth  = serverFacade.login(username,password);
        state.setAuthToken(auth.authToken());
        state.setUsername(auth.username());
        System.out.println("logged in as " + auth.username());
    }
    private void help() throws Exception {
        System.out.println("""
                Available commands: 
                register -create new account 
                login - sign into existing account
                help - show this message again 
                quit - exit program 
                """);
    }
}
