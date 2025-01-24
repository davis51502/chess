package chess.MoveFunctions;

import chess.*;

import java.util.HashSet;

public class pawnMove implements moveMove {

    public static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition currPosition) { //max is 16 moves for a pawn
        HashSet<ChessMove> moves = HashSet.newHashSet(16);
        int currX = currPosition.getColumn();
        int currY = currPosition.getRow();
        ChessPiece.PieceType[] promotionPieces = new ChessPiece.PieceType[]{null};

        ChessGame.TeamColor team = board.getTeamOfSquare(currPosition);
        int moveIncrement = team == ChessGame.TeamColor.WHITE ? 1 : -1;

        boolean promote = (team == ChessGame.TeamColor.WHITE && currY == 7) || (team == ChessGame.TeamColor.BLACK && currY == 2);
        if (promote) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
        }

        for (ChessPiece.PieceType promotionPiece : promotionPieces) {
            //is possible move forward
            ChessPosition forwardPosition = new ChessPosition(currY + moveIncrement, currX);
            if (moveMove.isValidSquare(forwardPosition) && board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(currPosition, forwardPosition, promotionPiece));
            }
            //if possible add left attack
            ChessPosition leftAttack = new ChessPosition(currY + moveIncrement, currX-1);
            if (moveMove.isValidSquare(leftAttack) &&
                    board.getPiece(leftAttack) != null &&
                    board.getTeamOfSquare(leftAttack) != team) {
                moves.add(new ChessMove(currPosition, leftAttack, promotionPiece));
            }
            //if possible add right attack
            ChessPosition rightAttack = new ChessPosition(currY + moveIncrement, currX+1);
            if (moveMove.isValidSquare(rightAttack) &&
                    board.getPiece(rightAttack) != null &&
                    board.getTeamOfSquare(rightAttack) != team) {
                moves.add(new ChessMove(currPosition, rightAttack, promotionPiece));
            }

            //first move double
            ChessPosition doubleForwardPosition = new ChessPosition(currY + moveIncrement*2, currX);
            if (moveMove.isValidSquare(doubleForwardPosition) &&
                    ((team == ChessGame.TeamColor.WHITE && currY == 2) || (team == ChessGame.TeamColor.BLACK && currY == 7)) &&
                    board.getPiece(doubleForwardPosition) == null &&
                    board.getPiece(forwardPosition) == null) {
                moves.add(new ChessMove(currPosition, doubleForwardPosition, promotionPiece));
            }

        }

        return moves;
    }

}
