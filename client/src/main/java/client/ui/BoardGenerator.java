package client.ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ClientState;

// prints the chessboard whilst using escape sequences
public class BoardGenerator {
    public void BoardGenerator(ClientState state) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        boolean white = (state.getWhichPlayerColor() == null
                || state.getWhichPlayerColor() == ChessGame.TeamColor.WHITE);
        System.out.println(EscapeSequences.ERASE_SCREEN);
        if (white) {

        }

    }
    private void drawTerminalBoard(ChessBoard board, int startRow, int rowInc, int startCol, int colEnd) {
        String[] headers= (startCol<colEnd) ?
                new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "}:
                new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};


    }
    private void drawFootHead(String[] headers) {
        System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("   ");
        for (String header: headers) {
            System.out.println(EscapeSequences.SET_TEXT_BOLD + EscapeSequences.SET_BG_COLOR_WHITE + header);}
        System.out.println(EscapeSequences.RESET_TEXT_BOLD_FAINT + "   ");
        System.out.println(EscapeSequences.ERASE_SCREEN);
    }
    private String
}
