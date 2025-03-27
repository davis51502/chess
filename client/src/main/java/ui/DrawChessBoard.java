package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class DrawChessBoard {

    public void drawBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor) {
        printRowLetters(teamColor);
        renderBoardRows(chessBoard, teamColor);
        printRowLetters(teamColor);
    }

    private void renderBoardRows(ChessBoard chessBoard, ChessGame.TeamColor teamColor) {
        int[] rowOrder = teamColor == ChessGame.TeamColor.WHITE
                ? new int[]{8, 7, 6, 5, 4, 3, 2, 1}
                : new int[]{1, 2, 3, 4, 5, 6, 7, 8};

        for (int row : rowOrder) {
            printColumnNumber(row, teamColor);
            renderBoardRow(chessBoard, row, teamColor);
            printColumnNumber(row, teamColor);
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }
    }

    private void renderBoardRow(ChessBoard chessBoard, int row, ChessGame.TeamColor teamColor) {
        for (int column = 1; column <= 8; column++) {
            // Background color
            System.out.print((row + column) % 2 == 0 ? SET_BG_COLOR_LIGHT_BROWN : SET_BG_COLOR_DARK_BROWN);

            // Get and render piece
            ChessPosition pos = calculatePosition(row, column, teamColor);
            ChessPiece piece = chessBoard.getPiece(pos);

            if (piece == null) {
                System.out.print(EMPTY);
                continue;
            }

            // Draw piece
            System.out.print(getPieceSymbol(piece));
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        return switch (piece.getPieceType()) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }

    private ChessPosition calculatePosition(int row, int column, ChessGame.TeamColor teamColor) {
        return new ChessPosition(
                teamColor == ChessGame.TeamColor.WHITE ? row : (9 - row),
                teamColor == ChessGame.TeamColor.WHITE ? column : (9 - column)
        );
    }

    private static void printColumnNumber(int row, ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);

        int displayRow = teamColor == ChessGame.TeamColor.WHITE ? (9 - row) : row;
        System.out.print(" " + displayRow + " ");
    }

    private static void printRowLetters(ChessGame.TeamColor teamColor) {
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(SET_BG_COLOR_DARK_GREY);

        String letters = "   \u2002\u2004a\u2003\u2002b\u2003\u2002\u200Ac"
                + "\u2003\u2002d\u2003\u2002\u200Ae\u2003\u2002f"
                + "\u2003\u2002\u200Ag\u2003\u2002\u200Ah\u2002\u2004   ";

        if (teamColor == ChessGame.TeamColor.WHITE) {
            System.out.print(letters);
        } else {
            StringBuilder reversed = new StringBuilder(letters);
            System.out.print(reversed.reverse().toString());
        }

        System.out.print(RESET_BG_COLOR);
        System.out.println();
    }
}