package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    /**
     * Draws the chess board with the current piece positions and highlights legal moves.
     *
     * @param chessBoard  The current state of the chess board.
     * @param teamColor   The perspective from which the board is drawn (white or black).
     * @param legalMoves  Positions to highlight as legal move destinations.
     */
    public void drawBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, Collection<ChessPosition> legalMoves) {
        // Print the file letters (a–h) at the top
        printRowLetters(teamColor);

        // Loop through each row of the board
        for (int row = 1; row <= 8; row++) {
            // Print rank number on the left
            printColumnNumber(row, teamColor);

            // Loop through each column of the board
            for (int column = 1; column <= 8; column++) {
                // Convert to correct board coordinates depending on team perspective
                ChessPosition pos = new ChessPosition(
                        teamColor == ChessGame.TeamColor.WHITE ? (9 - row) : row,
                        teamColor == ChessGame.TeamColor.BLACK ? (9 - column) : column);

                boolean legalMove = legalMoves.contains(pos);

                // Set background color based on square color and whether it's a legal move
                if ((row + column) % 2 == 0) {
                    System.out.print(legalMove ? SET_BG_COLOR_LIGHT_GREEN : SET_BG_COLOR_LIGHT_BROWN);
                } else {
                    System.out.print(legalMove ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_DARK_BROWN);
                }

                // Get and draw the piece at the current position
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece == null) {
                    System.out.print(EMPTY);
                    continue;
                }

                // Draw white pieces
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    switch (piece.getPieceType()) {
                        case KING -> System.out.print(WHITE_KING);
                        case QUEEN -> System.out.print(WHITE_QUEEN);
                        case ROOK -> System.out.print(WHITE_ROOK);
                        case BISHOP -> System.out.print(WHITE_BISHOP);
                        case KNIGHT -> System.out.print(WHITE_KNIGHT);
                        case PAWN -> System.out.print(WHITE_PAWN);
                        default -> System.out.print(EMPTY);
                    }
                }
                // Draw black pieces
                else {
                    switch (piece.getPieceType()) {
                        case KING -> System.out.print(BLACK_KING);
                        case QUEEN -> System.out.print(BLACK_QUEEN);
                        case ROOK -> System.out.print(BLACK_ROOK);
                        case BISHOP -> System.out.print(BLACK_BISHOP);
                        case KNIGHT -> System.out.print(BLACK_KNIGHT);
                        case PAWN -> System.out.print(BLACK_PAWN);
                        default -> System.out.print(EMPTY);
                    }
                }
            }

            // Print rank number on the right
            printColumnNumber(row, teamColor);
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }

        // Print the file letters (a–h) at the bottom
        printRowLetters(teamColor);
    }

    /**
     * Prints the rank number (1–8) on the side of the board.
     */
    private static void printColumnNumber(int row, ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);
        if (teamColor == ChessGame.TeamColor.WHITE) {
            System.out.print(" " + (9 - row) + " ");
        } else {
            System.out.print(" " + row + " ");
        }
    }

    /**
     * Prints the file letters (a–h) at the top or bottom of the board.
     */
    private static void printRowLetters(ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);

        // Spacing and letters between squares
        String letters = "   \u2002\u2004a\u2003\u2002b\u2003\u2002\u200Ac"
                + "\u2003\u2002d\u2003\u2002\u200Ae\u2003\u2002f"
                + "\u2003\u2002\u200Ag\u2003\u2002\u200Ah\u2002\u2004   ";

        if (teamColor == ChessGame.TeamColor.WHITE) {
            System.out.print(letters);
        } else {
            // Reverse the string for black's perspective
            System.out.print(new StringBuilder(letters).reverse().toString());
        }

        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }

    /**
     * Highlights all legal move destinations for a selected piece.
     *
     * @param chessBoard     The current board state.
     * @param teamColor      The color of the player making the move.
     * @param startPosition  The position of the piece to move.
     */
    public void drawHighlightedChessBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, ChessPosition startPosition) {
        ChessGame game = new ChessGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(teamColor);

        // Fetch all legal moves for the selected piece
        Collection<ChessMove> chessMoves = game.validMoves(startPosition);
        Collection<ChessPosition> legalMoves = new ArrayList<>();

        for (ChessMove move : chessMoves) {
            legalMoves.add(move.getEndPosition());
        }

        // Draw board with highlighted legal moves
        drawBoard(chessBoard, teamColor, legalMoves);
    }
}
