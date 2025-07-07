package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
    private final ChessGame.TeamColor pieceColor;
    public PawnMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> allPawnMoves = new ArrayList<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        int forwardMove;
        int startingRow;
        int promotionRow;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            forwardMove = 1;
            startingRow = 2;
            promotionRow = 8;
        } else {
            forwardMove = -1;
            startingRow = 7;
            promotionRow = 1;
        }
        // 1 - forward one square
        int oneStepRow = startRow + forwardMove; // 2 + 1 for white; 7-1 for black
        int targetCol = startCol;
        // check if square is one step ahead on board
        if (PieceMovesCalculator.isinBounds(oneStepRow, targetCol)) {
            ChessPosition oneStep = new ChessPosition(oneStepRow, targetCol);

            if (board.getPiece(oneStep) == null) {
                addCapture(myPosition, oneStepRow, promotionRow, oneStep, allPawnMoves);
                // 2 - forward two squares
                if (startRow == startingRow) {
                    int twoSteps = startRow + (2 * forwardMove);
                    if (PieceMovesCalculator.isinBounds(twoSteps, targetCol)) {
                        ChessPosition twoStepsPos = new ChessPosition(twoSteps, targetCol);
                        if (board.getPiece(twoStepsPos) == null) {
                            allPawnMoves.add(new ChessMove(myPosition, twoStepsPos, null));
                        }
                    }
                }
            }
        }
        // 3 - Diagonal capture moves
        int[] diagonalColChange = {-1, 1}; // column changing by -1 (left) or 1 (right)
        for (int colChange: diagonalColChange) {
            int captureRow = startRow + forwardMove;
            int captureCol = startCol + colChange;

            if (PieceMovesCalculator.isinBounds(captureRow, captureCol)) {
                ChessPosition capturePos = new ChessPosition(captureRow, captureCol);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);
                if (pieceAtCapture != null && pieceAtCapture.getTeamColor() != pieceColor) {
                    addCapture(myPosition, captureRow, promotionRow, capturePos, allPawnMoves);
                }
            }
        }
        return allPawnMoves;
    }

    private void addCapture(ChessPosition myPosition, int captureRow, int promotionRow, ChessPosition capturePos, Collection<ChessMove> allPawnMoves) {
        if (captureRow == promotionRow) {
            addPromotion(myPosition, capturePos, allPawnMoves);
        } else {
            allPawnMoves.add(new ChessMove(myPosition, capturePos, null));
        }
    }
    public void addPromotion(ChessPosition start, ChessPosition end, Collection<ChessMove> movesCollector) {
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }
}
