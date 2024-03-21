package client;

import java.util.Arrays;


public class ChessClient {

    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;

    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "register" -> register(params);
//                case "list" -> listPets();
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
//                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <username> <password>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
        }
        return """
                - help
                - logout
                - create game <game name>
                - list games
                - join game <game ID>
                - join observer <game ID>
                """;
    }

    public String signIn(String... params) throws Exception{
        if (params.length >= 2){
            String username = params[0];
            String password = params[1];
            server.signIn(username, password);
            state = State.SIGNEDIN;
        }
        else {
            throw new Exception("Error: Expected <username> <password>");

        }
        return "User Logged In.";
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            server.register(username, password, email);
            state = State.SIGNEDIN;
        }
        else {
            throw new Exception("Error: Expected <username> <password> <email>");

        }
        return "New User Created.";
    }

    public void assertSignedIn() throws Exception{
        if (state == State.SIGNEDOUT){
            throw new Exception("User must be signed in first");
        }
    }


}
