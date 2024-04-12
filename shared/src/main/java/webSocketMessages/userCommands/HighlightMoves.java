package webSocketMessages.userCommands;

import chess.ChessPosition;

public class HighlightMoves extends UserGameCommand{
    private ChessPosition pos;
    public HighlightMoves(String authToken, ChessPosition pos) {
        super(authToken);
        this.pos = pos;
    }

    public ChessPosition getPos() {
        return pos;
    }

}
