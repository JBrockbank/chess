package server.responses;

public class RegisterUserResponse extends BaseResponse{

    private String username;
    public String authToken;

    public RegisterUserResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }




}
