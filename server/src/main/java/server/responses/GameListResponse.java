package server.responses;

import model.GameData;

import java.util.Collection;

public class GameListResponse extends BaseResponse{

    private Collection<GameData> gameList;

    public GameListResponse(Collection<GameData> gameList){
        this.gameList = gameList;
    }

}
