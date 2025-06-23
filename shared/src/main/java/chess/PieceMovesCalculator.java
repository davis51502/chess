package chess;

import java.util.ArrayList;
import java.util.Collection;
// ensures any class with have 'pieceMoves' method
interface PieceMoveCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}

public class PieceMovesCalculator {

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) throws InvalidMoveException {
        // get the piece at the given position
        ChessPiece piece = board.getPiece(myPosition);
        return switch (piece.getPieceType()) {
            case KING -> new KingMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case QUEEN -> new QueenMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case BISHOP -> new BishopMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case KNIGHT -> new KnightMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case ROOK -> new RookMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            case PAWN -> new PawnMovesCalculator(piece.getTeamColor()).pieceMoves(board, myPosition);
            default -> throw new InvalidMoveException("Unknown piece type:" + piece.getPieceType());
        };
    }
    // "map checker": checks if a spot is actually on a chess board
    public static boolean isinBounds(int row, int col) {
        return row >= 1 && row <=8 && col >= 1 && col <=8;
    }
    // "straight liner": (good for bishops rooks and queen) straight line in one direction, goes step by step until it hits the edge of the board or another piece
    public Collection<ChessMove> straightLiner(int startRow, int startCol, ChessBoard board, int rowChange, int colChange, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> movesFound = new ArrayList<>(); // list for moves that were found can be moved in a straight line
        int currentRow = startRow + rowChange;
        int currentCol = startCol + colChange;
        while (isinBounds(currentRow, currentCol)) {
            // target position
            // piece at the target position
            // if the square is empty, good, we can move it there
            // else theres a piece there, we can capture it
            // break- if its an enemy or friendly, we can't move the piece through it
            // then restart the process and move piece to the next square for the next check
        }
        // return all moves that were found here right outside of while loop
        return movesFound;
    }
}