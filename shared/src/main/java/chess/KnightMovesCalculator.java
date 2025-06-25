package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private final ChessGame.TeamColor pieceColor;

    public KnightMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allKnightMoves  = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int[][] staticSteps = {
                {2, -1},
                {2,  1},
                {1,  2},
                { -1, 2},
                { -2,  1},
                { -2, -1},
                { -1,  -2},
                { 1,  -2}
        };
        for (int[] step : staticSteps) {
            int rowChange = step[0];
            int colChange = step[1];

            int newRow = startRow + rowChange;
            int newCol = startCol + colChange;

            if (PieceMovesCalculator.isinBounds(newRow, newCol)) {
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(newPos);
                if (targetPiece == null || targetPiece.getTeamColor() != this.pieceColor){
                    allKnightMoves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return allKnightMoves;
    }
}