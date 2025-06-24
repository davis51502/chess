package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    private final ChessGame.TeamColor pieceColor;

    public BishopMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allBishopMoves  = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        allBishopMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 1, 1, pieceColor));
        allBishopMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 1, -1, pieceColor));
        allBishopMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, -1, 1, pieceColor));
        allBishopMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, -1, -1, pieceColor));

        return allBishopMoves;
    }
}