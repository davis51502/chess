package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static final String EMPTY = "   ";
    private static final String WHITE_KING = EscapeSequences.WHITE_KING;
    private static final String WHITE_QUEEN = EscapeSequences.WHITE_QUEEN;
    private static final String WHITE_ROOK = EscapeSequences.WHITE_ROOK;
    private static final String WHITE_BISHOP = EscapeSequences.WHITE_BISHOP;
    private static final String WHITE_KNIGHT = EscapeSequences.WHITE_KNIGHT;
    private static final String WHITE_PAWN = EscapeSequences.WHITE_PAWN;

    private static final String BLACK_KING = EscapeSequences.BLACK_KING;
    private static final String BLACK_QUEEN = EscapeSequences.BLACK_QUEEN;
    private static final String BLACK_ROOK = EscapeSequences.BLACK_ROOK;
    private static final String BLACK_BISHOP = EscapeSequences.BLACK_BISHOP;
    private static final String BLACK_KNIGHT = EscapeSequences.BLACK_KNIGHT;
    private static final String BLACK_PAWN = EscapeSequences.BLACK_PAWN;

    // Maps for White & Black pieces
    private static final Map<ChessPiece.PieceType, String> WHITE_MAP = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> BLACK_MAP = new HashMap<>();

    static {
        // Populate White pieces map
        WHITE_MAP.put(ChessPiece.PieceType.KING, WHITE_KING);
        WHITE_MAP.put(ChessPiece.PieceType.QUEEN, WHITE_QUEEN);
        WHITE_MAP.put(ChessPiece.PieceType.ROOK, WHITE_ROOK);
        WHITE_MAP.put(ChessPiece.PieceType.BISHOP, WHITE_BISHOP);
        WHITE_MAP.put(ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT);
        WHITE_MAP.put(ChessPiece.PieceType.PAWN, WHITE_PAWN);
        WHITE_MAP.put(null, EMPTY);

        // Populate Black pieces map
        BLACK_MAP.put(ChessPiece.PieceType.KING, BLACK_KING);
        BLACK_MAP.put(ChessPiece.PieceType.QUEEN, BLACK_QUEEN);
        BLACK_MAP.put(ChessPiece.PieceType.ROOK, BLACK_ROOK);
        BLACK_MAP.put(ChessPiece.PieceType.BISHOP, BLACK_BISHOP);
        BLACK_MAP.put(ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT);
        BLACK_MAP.put(ChessPiece.PieceType.PAWN, BLACK_PAWN);
        BLACK_MAP.put(null, EMPTY);
    }

    private static final String[] HEADERS = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    private static final String[] HEADERS_REVERSED = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};

    public void drawBoard(ChessBoard chessBoard, ChessGame.TeamColor teamColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        // Print headers based on team color
        drawHeaders(out, teamColor == ChessGame.TeamColor.WHITE ? HEADERS : HEADERS_REVERSED);
        resetColor(out);

        // Draw board based on team color
        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawChessBoard(out, chessBoard);
        } else {
            drawChessBoardReversed(out, chessBoard);
        }

        // Print headers again
        drawHeaders(out, teamColor == ChessGame.TeamColor.WHITE ? HEADERS : HEADERS_REVERSED);
        resetColor(out);
    }

    private static void resetColor(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {
        for (String header : headers) {
            printHeaderText(out, header);
        }
        resetColor(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String header) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_PURPLE);
        out.print(header);
    }

    private static void setColor(PrintStream out, Boolean isLightSquare) {
        out.print(isLightSquare ? SET_BG_COLOR_DARK_BROWN : SET_BG_COLOR_LIGHT_BROWN);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard chessBoard) {
        boolean isLightSquare = true;
        for (int boardRow = 7; boardRow >= 0; --boardRow) {
            for (int squareCol = 0; squareCol < 10; ++squareCol) {
                isLightSquare = drawSquare(out, chessBoard, isLightSquare, boardRow, squareCol);
            }
            out.println();
            isLightSquare = !isLightSquare;
        }
    }

    private static void drawChessBoardReversed(PrintStream out, ChessBoard chessBoard) {
        boolean isLightSquare = true;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareCol = 0; squareCol < 10; ++squareCol) {
                isLightSquare = drawSquare(out, chessBoard, isLightSquare, boardRow, squareCol);
            }
            out.println();
            isLightSquare = !isLightSquare;
        }
    }

    private static boolean drawSquare(PrintStream out, ChessBoard chessBoard, boolean isLightSquare, int boardRow, int squareCol) {
        if (squareCol == 0 || squareCol == 9) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_PURPLE);
            out.print(" " + (boardRow + 1) + " ");
        } else {
            setColor(out, isLightSquare);
            isLightSquare = !isLightSquare;

            ChessPiece current = chessBoard.getPiece(new ChessPosition(boardRow + 1, squareCol));
            if (current == null) {
                out.print(EMPTY);
            } else if (current.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(WHITE_MAP.get(current.getPieceType()));
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(BLACK_MAP.get(current.getPieceType()));
            }
        }
        resetColor(out);
        return isLightSquare;
    }
}