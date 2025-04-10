package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    private static final String EMPTY = "   "; // 3 spaces to maintain consistent cell width

    public void drawBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, Collection<ChessPosition> legalMoves) {
        // Draw the board with consistent spacing
        printRowLetters(teamColor);
        for (int row = 8; row >= 1; row--) { // Reversed to count down from 8 to 1
            printColumnNumber(row, teamColor);
            for (int column = 1; column <= 8; column++) {
                ChessPosition pos = new ChessPosition(
                        teamColor == ChessGame.TeamColor.WHITE ? row : (9 - row),
                        teamColor == ChessGame.TeamColor.BLACK ? (9 - column) : column);
                boolean legalMove = legalMoves.contains(pos);

                // Background color
                if ((row + column) % 2 != 0) { // Flipped the checkerboard pattern
                    if (legalMove) {
                        System.out.print(SET_BG_COLOR_LIGHT_GREEN);
                    } else {
                        System.out.print(SET_BG_COLOR_LIGHT_BROWN);
                    }
                } else {
                    if (legalMove) {
                        System.out.print(SET_BG_COLOR_DARK_GREEN);
                    } else {
                        System.out.print(SET_BG_COLOR_DARK_BROWN);
                    }
                }

                // Get Piece
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece == null) {
                    System.out.print(EMPTY);
                    continue;
                }

                // Draw piece with consistent spacing
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    switch (piece.getPieceType()) {
                        case KING:
                            System.out.print(" " + WHITE_KING.trim() + " ");
                            break;
                        case QUEEN:
                            System.out.print(" " + WHITE_QUEEN.trim() + " ");
                            break;
                        case ROOK:
                            System.out.print(" " + WHITE_ROOK.trim() + " ");
                            break;
                        case BISHOP:
                            System.out.print(" " + WHITE_BISHOP.trim() + " ");
                            break;
                        case KNIGHT:
                            System.out.print(" " + WHITE_KNIGHT.trim() + " ");
                            break;
                        case PAWN:
                            System.out.print(" " + WHITE_PAWN.trim() + " ");
                            break;
                        default:
                            System.out.print(EMPTY);
                            break;
                    }
                } else {
                    switch (piece.getPieceType()) {
                        case KING:
                            System.out.print(" " + BLACK_KING.trim() + " ");
                            break;
                        case QUEEN:
                            System.out.print(" " + BLACK_QUEEN.trim() + " ");
                            break;
                        case ROOK:
                            System.out.print(" " + BLACK_ROOK.trim() + " ");
                            break;
                        case BISHOP:
                            System.out.print(" " + BLACK_BISHOP.trim() + " ");
                            break;
                        case KNIGHT:
                            System.out.print(" " + BLACK_KNIGHT.trim() + " ");
                            break;
                        case PAWN:
                            System.out.print(" " + BLACK_PAWN.trim() + " ");
                            break;
                        default:
                            System.out.print(EMPTY);
                            break;
                    }
                }
            }
            printColumnNumber(row, teamColor);
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }
        printRowLetters(teamColor);
    }

    private static void printColumnNumber(int row, ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);
        // Just print the row number directly
        System.out.print(" " + row + " ");
    }

    private static void printRowLetters(ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);

        // Use consistent spacing for column letters
        System.out.print("   ");
        char[] letters = teamColor == ChessGame.TeamColor.WHITE ?
                new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'} :
                new char[] {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};

        for (char letter : letters) {
            System.out.print(" " + letter + " ");
        }
        System.out.print("   ");

        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }

    public void drawHighlightedChessBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, ChessPosition startPosition) {
        ChessGame game = new ChessGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(teamColor);

        // Get the legal moves, then grab the end positions
        Collection<ChessMove> chessMoves = game.validMoves(startPosition);
        Collection<ChessPosition> legalMoves = new ArrayList<>();

        for (ChessMove move : chessMoves) {
            legalMoves.add(move.getEndPosition());
        }

        drawBoard(chessBoard, teamColor, legalMoves);
    }
}