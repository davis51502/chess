package chess;

import java.util.Collection;
// ensures any class with have 'pieceMoves' method
interface PieceMoveCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}

public class PieceMovesCalculator {

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) throws InvalidMoveException {
        // get the piece at the given position
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.getPieceType()) {
            case KING:
                return new KingMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case QUEEN:
                return new QueenMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case BISHOP:
                return new BishopMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case KNIGHT:
                return new KnightMovesCalculator(piece.getTeamColor()).pieceMoves(board,myPosition);
            case ROOK:
                return new RookMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case PAWN:
                return new PawnMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            default:
                throw new InvalidMoveException("Unknown piece type:" + piece.getPieceType());
        }
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    public Collection<ChessMove> directionAssure(int changeRow, int changeCol, ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}