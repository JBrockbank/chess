package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    public String errorMessage;

    public Error(String message){
        super(ServerMessageType.ERROR);
        this.errorMessage = "Error: " + message;
    }

    public String toString() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }



}
