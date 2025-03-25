package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final String EMPTY = "   ";

    // Piece Constants
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

    // Piece Maps
    private static final Map<ChessPiece.PieceType, String> WHITE_MAP = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> BLACK_MAP = new HashMap<>();

    // Static Initializer for Piece Maps
    static {
        populatePieceMap(WHITE_MAP, WHITE_KING, WHITE_QUEEN, WHITE_ROOK,
                WHITE_BISHOP, WHITE_KNIGHT, WHITE_PAWN);
        populatePieceMap(BLACK_MAP, BLACK_KING, BLACK_QUEEN, BLACK_ROOK,
                BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN);
    }

    // Header Constants
    private static final String[] HEADERS = {
            "   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "
    };
    private static final String[] HEADERS_REVERSED = {
            "   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "
    };

    // Helper method to populate piece maps
    private static void populatePieceMap(Map<ChessPiece.PieceType, String> map,
                                         String king, String queen, String rook,
                                         String bishop, String knight, String pawn) {
        map.put(ChessPiece.PieceType.KING, king);
        map.put(ChessPiece.PieceType.QUEEN, queen);
        map.put(ChessPiece.PieceType.ROOK, rook);
        map.put(ChessPiece.PieceType.BISHOP, bishop);
        map.put(ChessPiece.PieceType.KNIGHT, knight);
        map.put(ChessPiece.PieceType.PAWN, pawn);
        map.put(null, EMPTY);
    }

    // Drawing methods for white and black perspectives
    public static void drawWhite(chess.ChessBoard board) {
        var out = createOutputstream();
        out.print(ERASE_SCREEN);
        drawHeaders(out, HEADERS);
        resetColor(out);
        drawChessBoard(out, board);
        drawHeaders(out, HEADERS);
        resetColor(out);
    }

    public static void drawBlack(chess.ChessBoard board) {
        var out = createOutputstream();
        out.print(ERASE_SCREEN);
        drawHeaders(out, HEADERS_REVERSED);
        resetColor(out);
        drawChessBoardReversed(out, board);
        drawHeaders(out, HEADERS_REVERSED);
        resetColor(out);
    }

    // Utility methods
    private static PrintStream createOutputstream() {
        return new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    private static void resetColor(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {
        for (String header : headers) {
            printHeaderText(out, header);
        }
        resetColor(out);
        out.println();
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_PINK);
        out.print(player);
    }

    private static void setColor(PrintStream out, Boolean whatTurn) {
        out.print(whatTurn ? SET_BG_COLOR_RED : SET_BG_COLOR_DARK_GREEN);
    }

    private static boolean switchTurn(boolean whatTurn) {
        return !whatTurn;
    }

    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            for (int squareRow = 0; squareRow < 10; ++squareRow) {
                turn = processBoardSquare(out, chessBoard, turn, boardRow, squareRow);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

    private static void drawChessBoardReversed(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareRow = 9; squareRow > -1; --squareRow) {
                turn = processBoardSquare(out, chessBoard, turn, boardRow, squareRow);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

    private static boolean processBoardSquare(PrintStream out, chess.ChessBoard chessBoard,
                                              boolean turn, int boardRow, int squareRow) {
        if (squareRow == 0 || squareRow == 9) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_LIGHT_PINK);
            out.print(" " + (boardRow + 1) + " ");
        } else {
            setColor(out, turn);
            turn = switchTurn(turn);

            ChessPiece current = chessBoard.getPiece(new ChessPosition(boardRow + 1, squareRow));
            if (current == null) {
                out.print(EMPTY);
            } else {
                out.print(current.getTeamColor() == ChessGame.TeamColor.WHITE
                        ? SET_TEXT_COLOR_WHITE
                        : SET_TEXT_COLOR_BLACK);
                out.print(current.getTeamColor() == ChessGame.TeamColor.WHITE
                        ? WHITE_MAP.get(current.getPieceType())
                        : BLACK_MAP.get(current.getPieceType()));
            }
        }
        resetColor(out);
        return turn;
    }
}