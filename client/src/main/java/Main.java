import chess.*;
// this is the entry point
public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String serverURL = args.length > 0 ? args[0] : "https://localhost:3000";
    }
}