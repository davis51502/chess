package websocket.messages;

import chess.ChessBoard;

public class LoadGameMessage extends ServerMessage {
    private final ChessBoard board;