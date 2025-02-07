# My notes
Phase 0:
Why Add private final int row; and private final int col;?
These fields allow the class to retain this information 

ChessBoard: Represents the chessboard and its state.
ChessGame: Likely holds game-wide constants like team colors.
ChessMove: Represents a single move in the game.
ChessPosition: Represents a position on the chessboard (row and column).
HashSet: A data structure used to store unique moves without duplicates.

Phase 1:
*ChessGame represents an immediately playable board with the pieces in their default locations and the starting player set to WHITE.
removing moves returned from ChessPiece.validMoves() that violate game rules.

Extra Credit Moves: 
Castling
En passant (in passing) 