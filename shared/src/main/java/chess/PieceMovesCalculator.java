package chess;

import java.util.Collection;

public class PieceMovesCalculator {
    public ChessPiece.PieceType type;
    public ChessGame.TeamColor color;

    public PieceMovesCalculator(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    public Collection<ChessMove> directionAssure(int changeRow, int changeCol, ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}