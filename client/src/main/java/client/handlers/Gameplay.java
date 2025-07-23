package client.handlers;

import client.ClientState;
import client.ui.BoardGenerator;

// redraw leave and help
public class Gameplay {
    private final ClientState state ;
    private final BoardGenerator boardGenerator;
    public Gameplay(ClientState state, BoardGenerator boardGenerator) {
        this.state = state;
        this.boardGenerator = boardGenerator;
    }
    public void handle(String... args) {
        String command = args[0].toLowerCase();
        switch (command)  {
            case "redraw" -> boardGenerator.drawBoard(state);
            case"leave" -> leave();
            case"help" -> help();
            default -> System.out.println("invalid gameplay command, valid commands are: redraw, leave, help");
        }

    }
    private void leave() {
        state.setInGame(false);
        state.setCurrentGame(null);
        state.setPlayerColor(null);
        System.out.println("you've left the game! :D");

    }
    private void help() {
        System.out.println("""
                Available commands: 
                redraw- redraw chess board
                leave- leave current game 
                help- show this message
                """);

    }
}
