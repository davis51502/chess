package chess.MoveFunctions;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class queenMove implements moveMove {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        int[][] moveDirections = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);

        return moveMove.generateDirectionalMoves(board, currPosition, moveDirections, currY, currX, team);
    }

}
