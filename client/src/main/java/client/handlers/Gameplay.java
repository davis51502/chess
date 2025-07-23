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

    }
    private void leave() {

    }
    private void help() {

    }
}
