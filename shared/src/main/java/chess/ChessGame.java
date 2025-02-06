package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamColor;
    private ChessBoard board = new ChessBoard();
    private boolean isGameOver = false;
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
        if (observedPiece == null) {return null;}
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
        ChessPiece observedPiece = this.board.getPiece(move.getStartPosition());
        if (legalMoves == null) {
            throw new InvalidMoveException("legal moves null");
        }
        if (legalMoves.contains(move)){
            if (observedPiece.getTeamColor() != this.teamColor) {
                throw new InvalidMoveException("not your turn yet");
            }
            else {
                this.board.addPiece(move.getStartPosition(), null);
                if (move.getPromotionPiece() == null) {
                    this.board.addPiece(move.getEndPosition(), observedPiece);
                } else {
                    ChessPiece promotionPiece = new ChessPiece(observedPiece.getTeamColor(), move.getPromotionPiece());
                    this.board.addPiece(move.getEndPosition(), promotionPiece);
                } changeOfTurn();
            }
        }else{throw new InvalidMoveException("illegal moves");}
    }
    public void changeOfTurn(){
        if (this.teamColor == TeamColor.WHITE) {this.teamColor = TeamColor.BLACK;}else{this.teamColor=TeamColor.WHITE;}
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard observedBoard = this.board;
        ChessPosition kingsChair = kingFinder(teamColor);
        for (int row = 0; row <8; row++) {
            for (int col = 0; col <8; col++) {
                ChessPosition observedPosition = new ChessPosition(row+1, col+1);
                ChessPiece observedPiece = observedBoard.getPiece(observedPosition);
                if (observedPiece == null) {
                    continue;
                }
                if (observedPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> enemyMoves = observedPiece.pieceMoves(observedBoard, observedPosition);
                    if (kingFinderChecker(enemyMoves, kingsChair)){
                        return true;
                    }
                }
            }
        }return false;
    }

    public ChessPosition kingFinder (TeamColor teamColor){
        for (int row =0; row<8;row++) {
            for (int col = 0; col <8; col++){
                ChessPosition observedPosition = new ChessPosition(row+1, col+1);
                ChessPiece observedPiece = this.board.getPiece(observedPosition);
                if (observedPiece == null){
                    continue;
                }
                if (observedPiece.getPieceType() == ChessPiece.PieceType.KING && observedPiece.getTeamColor() == teamColor){
                    return observedPosition;
                }
            }
        }return null;
    }
    private boolean kingFinderChecker(Collection<ChessMove> enemyMoves, ChessPosition kingsChair){
        for (ChessMove move : enemyMoves){
            if (move.getEndPosition().equals(kingsChair)){
                return true;
            }
        }
        return false;
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
                if(observedPiece != null && observedPiece.getTeamColor() == teamColor && legalMoves.isEmpty()) {
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
        throw new RuntimeException("Not implemented");
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
}
