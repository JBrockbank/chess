package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    public int gameID;
    public Resign(String authToken, int ID) {
        super(authToken);
        this.gameID = ID;
    }

    public int getGameID() {
        return gameID;
    }

}
