package client;

import java.util.Scanner;

// runs command input loop- read eval print loop - repl
public class Repl {
    private final ChessClient client;
    private final Scanner scanner = new Scanner(System.in);
    public Repl(ChessClient client) {
        this.client = client;
    }
    public void run() {
        while (true) {
            System.out.println(client.getPrompt());
            String newLine = scanner.nextLine();
            String[] args = newLine.trim().split("\\s+");
            if (args.length > 0 && !args[0].isEmpty()) {
                client.eval(args);
            }
        }
    }
}
