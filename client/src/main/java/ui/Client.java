package ui;

public class Client {
    public static void main(String[] args) {
        var serverUrl = "https://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

    }
}