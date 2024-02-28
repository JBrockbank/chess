package server.responses;

public class ErrorResponse extends BaseResponse{

    private String message;

    public ErrorResponse(String message){
        this.message = message;
    }

}
