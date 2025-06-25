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
        int startCol = myPosition.getRow();

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
        return allPawnMoves;
    }
    private void addPromotion(ChessPosition start, ChessPosition end, Collection<ChessMove> movesCollector) {
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        movesCollector.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
    }
}
