package webSocketMessages.userCommands;

public class LeaveGame extends UserGameCommand{

    private int gameID;
    public LeaveGame(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
