package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] pieces = new ChessPiece[8][8];
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return pieces[position.getRow()-1][position.getColumn()-1];
    }

/*
    public void removePiece(ChessPosition position) {
        pieces[position.getRow()-1][position.getColumn()-1] = null;
    }
*/

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        pieces = new ChessPiece[8][8];
        ChessPiece.PieceType[] orderedPiece = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };
        // white
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(1, col+1), new ChessPiece(ChessGame.TeamColor.WHITE, orderedPiece[col]));
            addPiece(new ChessPosition(2, col+1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        // black
        for (int col = 0; col < 8; col ++) {
            addPiece(new ChessPosition(8, col +1), new ChessPiece(ChessGame.TeamColor.BLACK, orderedPiece[col]));
            addPiece(new ChessPosition(7, col +1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "pieces=" + Arrays.toString(pieces) +
                '}';
    }

    public ChessBoard copy() {
        ChessBoard newBoard = new ChessBoard();
        for (int i= 0; i <8; i++) {
            for (int j= 0; j<8; j++) {
                newBoard.pieces[i][j] = this.pieces[i][j];
            }
        }
        return newBoard;
    }
}