package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    public RookMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allRookMoves  = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        allRookMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 1, 0, pieceColor));
        allRookMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 0, 1, pieceColor));
        allRookMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, -1, 0, pieceColor));
        allRookMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 0, -1, pieceColor));
        return allRookMoves;
    }
}