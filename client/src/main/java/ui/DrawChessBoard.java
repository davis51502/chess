package ui;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    public void drawBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, Collection<ChessPosition> legalMoves) {
        printRowLetters(false);

        for (int row = 8; row >= 1; row--) {
            printColumnNumber(row);
            for (int column = 1; column <= 8; column++) {
                ChessPosition pos = new ChessPosition(
                        teamColor == ChessGame.TeamColor.WHITE ? (9 - row) : row,
                        teamColor == ChessGame.TeamColor.BLACK ? (9 - column) : column);

                boolean legalMove = legalMoves.contains(pos);

                // Background color
                if ((row + column) % 2 == 0) {
                    System.out.print(legalMove ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_BROWN);
                } else {
                    System.out.print(legalMove ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_DARK_BROWN);
                }

                // Get and draw piece - ensuring consistent width
                ChessPiece piece = chessBoard.getPiece(pos);
                if (piece == null) {
                    System.out.print("   "); // three spaces for empty squares to match piece width
                } else {
                    drawPiece(piece);
                }
            }
            System.out.print(" ");
            printColumnNumber(row);
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }

        printRowLetters(false);
    }

    private void drawPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case KING -> System.out.print(WHITE_KING);
                case QUEEN -> System.out.print(WHITE_QUEEN);
                case ROOK -> System.out.print(WHITE_ROOK);
                case BISHOP -> System.out.print(WHITE_BISHOP);
                case KNIGHT -> System.out.print(WHITE_KNIGHT);
                case PAWN -> System.out.print(WHITE_PAWN);
                default -> System.out.print("  "); // Two spaces for consistency
            }
        } else {
            switch (piece.getPieceType()) {
                case KING -> System.out.print(BLACK_KING);
                case QUEEN -> System.out.print(BLACK_QUEEN);
                case ROOK -> System.out.print(BLACK_ROOK);
                case BISHOP -> System.out.print(BLACK_BISHOP);
                case KNIGHT -> System.out.print(BLACK_KNIGHT);
                case PAWN -> System.out.print(BLACK_PAWN);
                default -> System.out.print("  "); // Two spaces for consistency
            }
        }
    }

    private static void printColumnNumber(int row) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(" " + row + " ");
    }

    private static void printRowLetters(boolean reversed) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (reversed) {
            reverseArray(letters);
        }

        System.out.print("   "); // Padding before letters
        for (String letter : letters) {
            System.out.print(" " + letter + " ");
        }
        System.out.print("   "); // Padding after letters

        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }

    private static void reverseArray(String[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            String temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    public void drawHighlightedChessBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor, ChessPosition startPosition) {
        ChessGame game = new ChessGame();
        game.setBoard(chessBoard);
        game.setTeamTurn(teamColor);

        Collection<ChessMove> chessMoves = game.validMoves(startPosition);
        Collection<ChessPosition> legalMoves = new ArrayList<>();

        for (ChessMove move : chessMoves) {
            legalMoves.add(move.getEndPosition());
        }

        drawBoard(chessBoard, teamColor, legalMoves);
    }
}
