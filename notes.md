# My notes
### Phase 0: Chess Moves

**Chess Board**
- Manages the current state of the board.
- Includes methods to add or remove pieces for testing.
- Has a method to reset the board.

**Chess Piece**
- Represents a single chess piece. Defined by its type and color.
- Uses a PieceType enumeration to define different types of pieces.
- Defines rules for how a piece moves, independent of other rules like check or checkmate state.
- The Piece Moves method returns the moves a piece can legally make.


**Chess Move**
- Represents a possible move a chess piece could make.
- Contains starting and ending positions.
- Can include a pawn promotion type of piece


**Chess Position**
- Represents a location on the chessboard.
- Defined by a row number and a column number.
- Constructor parameters bind to row and column fields.

