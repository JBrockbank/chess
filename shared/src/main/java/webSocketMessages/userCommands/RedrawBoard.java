package webSocketMessages.userCommands;

public class RedrawBoard extends UserGameCommand{
    private int gameID;

    public RedrawBoard(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.REDRAW;
        this.gameID = gameID;
    }

    public int getGameID(){
        return gameID;
    }
}