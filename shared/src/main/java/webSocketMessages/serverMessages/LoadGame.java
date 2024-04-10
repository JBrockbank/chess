package webSocketMessages.serverMessages;

import model.GameData;

public class LoadGame extends ServerMessage{

    public GameData game;
    public LoadGame(ServerMessageType type) {
        super(type);
    }

    public LoadGame(ServerMessageType type, String message) {
        super(type, message);
    }



    public GameData getGameData() {
        return game;
    }




}
