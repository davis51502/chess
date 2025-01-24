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
    private  ChessPiece[][] layoutOfBoard;

    public ChessBoard() {
        layoutOfBoard = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        layoutOfBoard[position.getColumn()-1][position.getRow()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return layoutOfBoard[position.getColumn()-1][position.getRow()-1];
    }

    public ChessGame.TeamColor getTeamOfSquare(ChessPosition position) {
        if (getPiece(position) != null) {
            return getPiece(position).getTeamColor();
        }
        else return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        layoutOfBoard = new ChessPiece[8][8];
        ChessPiece.PieceType[] piecesOrder = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };

        //add white 
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(1, col + 1), new ChessPiece(ChessGame.TeamColor.WHITE, piecesOrder[col]));
            addPiece(new ChessPosition(2, col + 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }

        //add black 
        for (int col = 0; col < 8; col++) {
            addPiece(new ChessPosition(8, col + 1), new ChessPiece(ChessGame.TeamColor.BLACK, piecesOrder[col]));
            addPiece(new ChessPosition(7, col + 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(layoutOfBoard, that.layoutOfBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(layoutOfBoard);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "layoutOfBoard=" + Arrays.toString(layoutOfBoard) +
                '}';
    }
}