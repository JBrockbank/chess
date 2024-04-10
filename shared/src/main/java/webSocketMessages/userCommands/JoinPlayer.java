package webSocketMessages.userCommands;

import chess.ChessGame;
import model.GameData;

public class JoinPlayer extends UserGameCommand{

    Integer gameID;
    ChessGame.TeamColor playerColor;
    GameData game;

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor, GameData game) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = CommandType.JOIN_PLAYER;
        this.game = game;
    }
    public Integer getID(){
        return this.gameID;
    }

    public ChessGame.TeamColor getColor(){
        return this.playerColor;
    }

    public GameData getGame() {
        return this.game;
    }

}
