package chess;
import chess.MoveFunctions.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor team;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
            this.team = pieceColor;
            this.type = type;
        }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case KING -> kingMove.getMoves(board, myPosition);
            case QUEEN -> queenMove.getMoves(board, myPosition);
            case BISHOP -> bishopMove.getMoves(board, myPosition);
            case KNIGHT -> knightMove.getMoves(board, myPosition);
            case ROOK -> rookMove.getMoves(board, myPosition);
            case PAWN -> pawnMove.getMoves(board, myPosition);
        };


    }    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "team=" + team +
                ", type=" + type +
                '}';
    }
}
