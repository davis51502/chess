package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamColor;
    private ChessBoard board = new ChessBoard();
    public boolean isResigned = false;
    public ChessGame() {
        this.teamColor = TeamColor.WHITE;
        this.board.resetBoard();
    }
    public ChessGame(TeamColor teamColor, ChessBoard board) {
        this.teamColor = teamColor;
        this.board = board;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece observedPiece = this.board.getPiece(startPosition);
        if (observedPiece == null) {
            return new ArrayList<>();
        }
        Collection<ChessMove> legalMoves = observedPiece.pieceMoves(this.board, startPosition);
        Collection<ChessMove> newLegalMoves = new ArrayList<>();
        for (ChessMove move : legalMoves) {
            ChessBoard copyOfBoard = copyBoard(this.board);
            copyOfBoard.addPiece(move.getStartPosition(), null);
            copyOfBoard.addPiece(move.getEndPosition(), observedPiece);
            ChessGame updatedGame = new ChessGame(this.teamColor, copyOfBoard);

            if (!updatedGame.isInCheck(observedPiece.getTeamColor())){
                newLegalMoves.add(move);
            }
        }
        return newLegalMoves;
    }
    public ChessBoard copyBoard(ChessBoard board) {
        ChessBoard copyOfBoard = new ChessBoard();
        for (int row=0; row<8; row++) {
            for (int col =0; col <8; col++) {
                ChessPosition position = new ChessPosition(row+1, col+1);
                ChessPiece piece = board.getPiece(position);
                copyOfBoard.addPiece(position, piece);

            }
        }return copyOfBoard;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> legalMoves = this.validMoves(move.getStartPosition());
        if (legalMoves == null) {
            throw new InvalidMoveException("legal moves are null");
        }
        if (!legalMoves.contains(move)){ throw new InvalidMoveException("illegal move"); }
            ChessPiece observedPiece = this.board.getPiece(move.getStartPosition());
            if (observedPiece.getTeamColor() != this.teamColor) {
                throw new InvalidMoveException("not your turn yet");
            }
            this.board.addPiece(move.getStartPosition(), null);
            ChessPiece pieceToPlace = (move.getPromotionPiece() == null) ? observedPiece :
                    new ChessPiece(observedPiece.getTeamColor(), move.getPromotionPiece());
            this.board.addPiece(move.getEndPosition(), pieceToPlace);
            changeOfTurn();
    }


    public void changeOfTurn(){
        this.teamColor = (this.teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingsChair = kingFinder(teamColor);
        if (kingsChair == null) { throw new IllegalStateException("king not found on board for team:"+teamColor); }
        for (int row = 0; row <8; row++) {
            for (int col = 0; col <8; col++) {
                ChessPiece observedPiece = this.board.getPiece(new ChessPosition(row+1, col+1));
                if (observedPiece == null || observedPiece.getTeamColor() == teamColor)  {
                    continue;
                }
                if (canAttackKing(observedPiece, row, col, kingsChair)) {
                        return true;
                    }
                }
            }
        return false;
    }
    private boolean canAttackKing(ChessPiece enemyPiece, int row, int col, ChessPosition kingsChair){
        ChessPosition observedPosition = new ChessPosition(row+1, col+1);
        Collection<ChessMove> enemyMoves = enemyPiece.pieceMoves(this.board, observedPosition);
        for (ChessMove move : enemyMoves){
            if (move.getEndPosition().equals(kingsChair)){
                return true;
            }
        }
        return false;
    }
    public ChessPosition kingFinder (TeamColor teamColor){
        for (int row =0; row<8;row++) {
            for (int col = 0; col <8; col++){
                ChessPiece observedPiece = this.board.getPiece(new ChessPosition(row+1, col+1));
                if (observedPiece != null && observedPiece.getPieceType() == ChessPiece.PieceType.KING && observedPiece.getTeamColor() == teamColor) {
                    return new ChessPosition(row+1, col+1);
                }
            }
        }return null; //king not found
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> legalMoves;
        if(!isInCheck(teamColor)) {
            return false;
        }
        for (int row = 0; row <8; row++) {
            for (int col = 0; col<8 ; col++){
                ChessPosition observedPosition = new ChessPosition(row+1, col+1);
                ChessPiece observedPiece = this.board.getPiece(observedPosition);
                legalMoves = validMoves(observedPosition);
                if(observedPiece != null && observedPiece.getTeamColor() == teamColor && (legalMoves != null && !legalMoves.isEmpty())) {
                    return false;
                }
            }
        }return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheckmate(teamColor))
        {
            return false;
        }
        for (int row =0; row<8; row++){
            for (int col=0; col<8; col++){
                ChessPosition observedPosition = new ChessPosition(row+1, col+1);
                ChessPiece observedPiece = this.board.getPiece(observedPosition);
                Collection<ChessMove> legalMoves = validMoves(observedPosition);
                if(observedPiece != null && observedPiece.getTeamColor()== teamColor && legalMoves != null && !legalMoves.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
    @Override
    public String toString() {
        return "ChessGame{" +
                "teamColor=" + teamColor +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamColor == chessGame.teamColor && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, board);
    }
}
