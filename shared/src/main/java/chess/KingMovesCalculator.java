package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    public KingMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allKingMoves  = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        int[][] staticSteps = {
                {-1, -1},
                {-1,  0},
                {-1,  1},
                { 0, -1},
                { 0,  1},
                { 1, -1},
                { 1,  0},
                { 1,  1}
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
                    allKingMoves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return allKingMoves;
    }
}
