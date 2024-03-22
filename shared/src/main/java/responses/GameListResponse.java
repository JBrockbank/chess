package responses;

import model.GameData;

import java.util.Collection;

public class GameListResponse extends BaseResponse{

    public Collection<GameData> games;

    public GameListResponse(Collection<GameData> games){
        this.games = games;
    }

}
