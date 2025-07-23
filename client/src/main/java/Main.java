import chess.*;
import client.ChessClient;

// this is the entry point
public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        String serverURL = "http://localhost:3000";
        if (args.length == 1) {
            serverURL = args[0];

        }
        new ChessClient(serverURL).run();
    }}