package webSocketMessages.userCommands;

public class LeaveGame extends UserGameCommand{


    public LeaveGame(String authToken) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
    }
}
