package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessPiece.PieceType promopiece;
    public ChessPosition endpos;
    public ChessPosition startpos;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startpos = startPosition;
        this.endpos = endPosition;
        this.promopiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startpos;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {

        return this.endpos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {

        return this.promopiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return promopiece == chessMove.promopiece && Objects.equals(endpos, chessMove.endpos) && Objects.equals(startpos, chessMove.startpos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promopiece, endpos, startpos);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "promopiece=" + promopiece +
                ", endpos=" + endpos +
                ", startpos=" + startpos +
                '}';
    }
}
