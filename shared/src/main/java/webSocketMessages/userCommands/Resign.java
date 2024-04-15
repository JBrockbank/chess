package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    public int gameID;
    public Resign(String authToken, int id) {
        super(authToken);
        this.gameID = id;
        this.commandType = CommandType.RESIGN;
    }

    public int getGameID() {
        return gameID;
    }

}
