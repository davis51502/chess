package client.ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientState;

// prints the chessboard whilst using escape sequences
public class BoardGenerator {
    public void drawBoard(ClientState state) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        boolean white = (state.getWhichPlayerColor() == null
                || state.getWhichPlayerColor() == ChessGame.TeamColor.WHITE);
        System.out.print(EscapeSequences.ERASE_SCREEN);
        if (white) {
            drawTerminalBoard(board, 8, -1, 1, 9);
        } else {drawTerminalBoard(board, 1,1,8,0);}
    }
    /**
     * Draws a chess board to the console -logic for rendering the board.
     * @param board holds state of all the pieces
     * @param startRow row to start drawing from- depends on white or blacks perspective
     * @param rowInc The direction to move through the rows, -1 is down from 8, +1 goes up from 1
     * @param startCol column to start drawing from
     * @param colEnd  column where the drawing of the board ends
     */
    private void drawTerminalBoard(ChessBoard board, int startRow, int rowInc, int startCol, int colEnd) {
        String[] headers= (startCol<colEnd) ?
                new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "}:
                new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        drawFootHead(headers);
        //loop thru each row of the board
        for (int rx = startRow; rx != startRow + (rowInc * 8); rx += rowInc) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + " " + rx + " " + EscapeSequences.RESET_TEXT_COLOR);

            for (int cx = startCol; cx != colEnd; cx += (startCol < colEnd ? 1 : -1)) {
                boolean isItALightSquare = (rx + cx) % 2 != 0;
                String backgroundColor = isItALightSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY
                        : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                //get piece at current row/column
                ChessPiece piece = board.getPiece(new ChessPosition(rx, cx));
                System.out.print(backgroundColor + getPieceType(piece) + EscapeSequences.RESET_BG_COLOR);
            }

            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE + " " + rx + " " + EscapeSequences.RESET_TEXT_COLOR);
        }
        drawFootHead(headers);
    }
    private void drawFootHead(String[] headers) {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + "   ");
        for (String header: headers) {
            System.out.print(EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_BG_COLOR_MAGENTA + header + EscapeSequences.RESET_BG_COLOR);
        }
        System.out.println(EscapeSequences.RESET_TEXT_BOLD_FAINT + "   ");
    }
    private String getPieceType(ChessPiece piece) {
        if (piece == null) return "   "; // 3 spaces to match piece width
        return switch (piece.getTeamColor()) {
            case WHITE -> switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
            case BLACK -> switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        };
    }
}