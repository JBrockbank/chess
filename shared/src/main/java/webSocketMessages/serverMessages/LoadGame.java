package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage{

    public GameData game;
    public ChessGame.TeamColor teamColor;
    public LoadGame(ServerMessageType type) {
        super(type);
    }

    public LoadGame(ServerMessageType type, String message) {
        super(type, message);
    }



    public GameData getGameData() {
        return game;
    }
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }




}
