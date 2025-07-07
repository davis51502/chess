import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        int port = 3000;
        try {
            Server server = new Server();
            server.run(port);
            System.out.println("server running on " + port);
        } catch (Exception e) {
            System.err.println("failed to start sever on port" + port);
            e.printStackTrace();
        }

    }
}