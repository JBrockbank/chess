package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMove extends UserGameCommand {


    private ChessPosition startPos;
    private ChessPosition endPos;
    private ChessGame.TeamColor playerColor;
    private int gameID;
    public MakeMove(String authToken, ChessPosition startPos, ChessPosition endPos, int gameID, ChessGame.TeamColor color) {
        super(authToken);
        this.startPos = startPos;
        this.endPos = endPos;
        this.gameID = gameID;
        this.playerColor = color;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public ChessPosition getStartPos() {
        return startPos;
    }

    public ChessPosition getEndPos() {
        return endPos;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
