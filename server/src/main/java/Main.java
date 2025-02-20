import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server server = new Server();
        int port = server.run(8080);

        // confirm server is running
        if (port != -1) {
            System.out.println("Server is running on http://localhost:" + port);
        } else {
            System.err.println("Failed to start the server.");
        }
    }
}
