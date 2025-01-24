package chess.MoveFunctions;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class kingMove implements moveMove {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) {
        int[][] relativeMoves = {{-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};
        return moveMove.generateStaticMoves(currPosition, relativeMoves, board);

    }

}
