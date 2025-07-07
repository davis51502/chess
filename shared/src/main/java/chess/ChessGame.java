package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;
    private boolean isGameOver = false;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    public ChessGame(TeamColor teamTurn, ChessBoard simulatedBoard) {
        this.teamTurn = teamTurn;
        this.board = simulatedBoard;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        this.teamTurn = team;
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
        ChessPiece piece = board.getPiece(startPosition);
        // Make sure valid piece is there and its the right teams turn
        if (piece == null) {
            return new ArrayList<>();
        }

        // 1: get all the possible moves for piece
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> authMoves = new ArrayList<>();

        // 2: filter out moves that leave the king in check
        for (ChessMove move : potentialMoves) {
            // make deep copy of board to simulate the move
            ChessBoard simulatedBoard = this.board.copy();
            ChessPiece movingPiece = simulatedBoard.getPiece(move.getStartPosition());
            // make a simulated move from starting position to end position
            simulatedBoard.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() != null) {
                ChessPiece promotedPiece = new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece());
                simulatedBoard.addPiece(move.getEndPosition(), promotedPiece);
            } else {
                simulatedBoard.addPiece(move.getEndPosition(), movingPiece);
            }
            // create temporary ChessGame to check state after the move
            ChessGame fakeGame = new ChessGame(piece.getTeamColor(), simulatedBoard);
            // check if current team's king is in check after simulated move
            if (!fakeGame.isInCheck(piece.getTeamColor())) {
                authMoves.add(move); // if king isn't in check, its a valid move
            }
        }
        return authMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        if (possibleMoves == null || !possibleMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: Not a legal move for piece/current team!");
        }
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        if (pieceToMove == null) {
            throw new InvalidMoveException("Invalid Move: No piece at start position!");
        }
        if (pieceToMove.getTeamColor() != this.teamTurn) {
            throw new InvalidMoveException("Invalid Move: its not your turn to move that piece!");
        }
        // make move on board
        board.addPiece(move.getEndPosition(), pieceToMove);
        board.addPiece(move.getStartPosition(), null);
        // pawn promotion handler
        if (move.getPromotionPiece() != null) {
            ChessPiece promotePiece = new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promotePiece);
        }
        // enpassant and castling here?
        // switch teams
        this.teamTurn = (this.teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null) {
            return false;
        }
        for (int k = 1;k<=8; k++) {
            for (int m =1; m<=8; m++) {
                ChessPosition currentPos = new ChessPosition(k,m);
                ChessPiece piece = board.getPiece(currentPos);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> oppMoves = piece.pieceMoves(board, currentPos);
                    // check if any moves target king
                    for (ChessMove move : oppMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true; // king is in check
                        }
                    }
                }
            }
        }
        return false; // king isn't in check
    }
    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(currentPos);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return currentPos;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return mimicBoard(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false; // in check so it can't be stalemate
        }
        return mimicBoard(teamColor);
    }

    private boolean mimicBoard(TeamColor teamColor) {
        for (int i = 1; i <=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition currentPos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(currentPos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    // return moves that don't leave king in check
                    Collection<ChessMove> moves = validMoves(currentPos);
                    if (moves != null && !moves.isEmpty()) {
                        return false; // found a valid move since its not null nor empty
                    }
                }
            }
        }
        return true; // in check and has no valid moves
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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return isGameOver == chessGame.isGameOver && Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn, isGameOver);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                ", isGameOver=" + isGameOver +
                '}';
    }
}
