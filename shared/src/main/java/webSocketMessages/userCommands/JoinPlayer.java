package webSocketMessages.userCommands;

import chess.ChessGame;
import model.GameData;

public class JoinPlayer extends UserGameCommand{

    Integer gameID;
    ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }
    public Integer getID(){
        return this.gameID;
    }

    public ChessGame.TeamColor getColor(){
        return this.playerColor;
    }


}
