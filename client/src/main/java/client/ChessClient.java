package client;

import model.GameData;

import java.util.Arrays;


public class ChessClient {

    private String authToken = null;
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
                case "creategame" -> createGame(params);
                case "logout" -> logout();
                case "joingame" -> joinGame(params);
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
                - createGame <game name>
                - listGames
                - joinGame <game ID> <player color>
                - joinObserver <game ID>
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
        return "User logged in.";
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
        return "New user created. User logged in.";
    }


    public String logout() throws Exception {
        assertSignedIn();
        System.out.println("Logging out");
        server.logout();
        state = State.SIGNEDOUT;
        return "Signed out";
    }


    public String createGame(String...params) throws Exception {
        assertSignedIn();
        if (params.length >= 1){
            String name = params[0];
            System.out.println("Creating Game: " + name);
            server.createGame(name);
        }
        else {
            throw new Exception("Error: Expected <game name>");
        }
        return "New game created";
    }

    public void assertSignedIn() throws Exception{
        if (state == State.SIGNEDOUT){
            throw new Exception("User must be signed in first");
        }
    }


    public String joinGame(String...params) throws Exception {
        assertSignedIn();
        if (params.length >= 2) {
            String gameID = params[0];
            String playerColor = params[1];
            int ID = 0;
            ID = Integer.parseInt(gameID);
            playerColor = playerColor.toUpperCase();
            GameData gameData = server.joinGame(ID, playerColor);
            return displayGame(gameData);
        }
        else {
            throw new Exception("Error: Expected <gameID>");
        }
    }



    public String displayGame(GameData gameData){
        var output = gameData.gameName() + ":\n";
        return output;
    }



}
