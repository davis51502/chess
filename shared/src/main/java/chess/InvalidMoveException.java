package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(int i, String s) {}

    public InvalidMoveException(String message) {
        super(message);
    }
}
