package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allQueenMoves  = new ArrayList<>();

        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        allQueenMoves.addAll(PieceMovesCalculator.straightLiner(startRow, startCol, board, 1, 0, pieceColor));

    }
}
