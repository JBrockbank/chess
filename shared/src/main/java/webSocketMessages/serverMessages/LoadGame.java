package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;

public class LoadGame extends ServerMessage {
    public GameData game;
//    private ChessGame.TeamColor color;


    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
//        this.color = color;
    }

    public GameData getGame() {
        return game;
    }

    public String toString() {
        return "Load Game";
    }

//    public ChessGame.TeamColor getColor(){
//        return color;
//    }

}