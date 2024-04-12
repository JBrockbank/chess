package client;

import chess.*;
import client.WebSocket.WebSocketFacade;
import model.GameData;
import ui.DrawBoard;
import webSocketMessages.ResponseException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;


public class ChessClient {

    private final ServerFacade server;
    private final String serverUrl;
    private List<GameData> gameDataList = new ArrayList<>();
    private WebSocketFacade ws;
    private DrawBoard draw;
    private String username;
    private int gameID;
    private String authToken;
    ChessGame.TeamColor playerColor;




    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(8080);
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
                case "listgames" -> listGames();
                case "observegame" -> observeGame(params);
                case "redrawboard" -> redrawChessBoard();
                case "leave" -> leave();
                case "makemove" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params);
                case "quit" -> "quit";
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
        else if (state == State.Game) {
            return """
                    Command not recognized\n
                    - redrawBoard
                    - leave
                    - makeMove <piece position> <new piece position>
                    - resign
                    - highlight <piece position> (Highlight Legal Moves)
                    """;
        }
        return """
                - help
                - logout
                - createGame <game name>
                - listGames
                - joinGame <game ID> <player color>
                - observeGame <game ID>
                """;
    }



    public String signIn(String... params) throws Exception{
        assertSignedOut();
        if (params.length >= 2){
            String username = params[0];
            String password = params[1];
            this.authToken = server.signIn(username, password);
            state = State.SIGNEDIN;
            this.username = username;
        }
        else {
            throw new Exception("Error: Expected <username> <password>");

        }
        return "User logged in.";
    }

    public String register(String... params) throws Exception {
        assertSignedOut();
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            this.authToken = server.register(username, password, email);
            state = State.SIGNEDIN;
            this.username = username;
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
        this.username = null;
        this.authToken = null;
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


    public String joinGame(String...params) throws Exception {
        assertSignedIn();
        if (params.length >= 2) {
            String gameIndex = params[0];
            String playerColor = params[1];
            int ID = 0;
            ID = Integer.parseInt(gameIndex);
            if (ID > gameDataList.size()){
                throw new Exception("Game doesn't exist");
            }
            this.gameID = gameDataList.get(ID-1).gameID();
            playerColor = playerColor.toUpperCase();
            GameData gameData = server.joinGame(gameID, playerColor);
//            displayGame(gameData);
            ChessGame.TeamColor color = convertColor(playerColor);
            this.playerColor = color;
            ws = new WebSocketFacade(serverUrl);
            ws.joinGame(username, gameID, color, gameData);
            state = State.Game;
            return "";
        }
        else {
            throw new Exception("Error: Expected <gameID>");
        }
    }




    public String observeGame(String...params) throws Exception {
        assertSignedIn();
        if (params.length >= 1) {
            String gameIndex = params[0];
            int ID = 0;
            ID = Integer.parseInt(gameIndex);
            if (ID > gameDataList.size()){
                throw new Exception("Game doesn't exist");
            }
            this.gameID = gameDataList.get(ID-1).gameID();
            GameData gameData = server.observeGame(gameID);
            ws = new WebSocketFacade(serverUrl);
            ws.joinObserver(username, gameID, gameData);
            state = State.Game;
            playerColor = null;
            return "";
        }
        else {
            throw new Exception("Error: Expected <gameID>");
        }
    }


    public String listGames() throws Exception {
        assertSignedIn();
        Collection<GameData> gameList = server.listGames();
        updateGameList(gameList);
        displayGameList();
        return "";
    }

    public String redrawChessBoard() throws Exception {
        assertInGame();
        ws.redrawBoard(username, gameID);
        return "";
    }

    public String leave() throws Exception {
        assertInGame();
        state = State.SIGNEDIN;
        ws = new WebSocketFacade(serverUrl);
        ws.leave(username);
        this.playerColor = null;
        this.gameID = 0;
        return "";
    }

    public String makeMove(String...params) throws Exception {
        assertInGame();
        System.out.print("Making Move");
        if (params.length >= 2) {
            String startPosString = params[0];
            char columnChar = startPosString.charAt(0);
            String rowChar = String.valueOf(startPosString.charAt(1));
            int startRow = Integer.parseInt(rowChar);
            int startCol = letterToNumber(columnChar);
            ChessPosition startPos = new ChessPosition(startRow, startCol);

            String endPosString = params[1];
            columnChar = endPosString.charAt(0);
            rowChar = String.valueOf(endPosString.charAt(1));
            int endRow = Integer.parseInt(rowChar);
            int endCol = letterToNumber(columnChar);
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            ws.makeMove(username, startPos, endPos, gameID, playerColor);
        }
        else {
            throw new Exception("Error: Expected makemove <piece position> <new piece position");
        }
        return "";
    }

    public int letterToNumber(char letter) throws Exception {
        if (letter == 'A' || letter == 'a'){
            return 1;
        }
        else if (letter == 'B' || letter == 'b'){
            return 2;
        }
        else if (letter == 'C' || letter == 'c'){
            return 3;
        }
        else if (letter == 'D' || letter == 'd'){
            return 4;
        }
        else if (letter == 'E' || letter == 'e'){
            return 5;
        }
        else if (letter == 'F' || letter == 'f'){
            return 6;
        }
        else if (letter == 'G' || letter == 'g'){
            return 7;
        }
        else if (letter == 'H' || letter == 'h'){
            return 8;
        }
        else {
            throw new Exception("Error: expected makemove <(A-H)(1-8)> <(A-H)(1-8)>");
        }
    }

    public String highlightLegalMoves(String...params) throws Exception {
        assertInGame();
        System.out.print("\nHighlighting legal moves");
        if (params.length >=1) {
            String posString = params[0];
            char columnChar = posString.charAt(0);
            String rowChar = String.valueOf(posString.charAt(1));
            int row = Integer.parseInt(rowChar);
            int col = letterToNumber(columnChar);
            ChessPosition pos = new ChessPosition(row,col);

            ws.highlightMoves(authToken, pos);

            //Find a way to get the updated Game Data

        }


        return "";
    }

    public String resign() throws Exception {
        assertInGame();
        return "";
    }



    public void assertSignedIn() throws Exception{
        if (state == State.SIGNEDOUT){
            throw new Exception("User must be signed in first");
        }
        else if (state == State.Game){
            throw new Exception("User must leave game first");
        }
    }

    private void assertSignedOut() throws Exception {
        if (state != State.SIGNEDOUT){
            throw new Exception("User must be signed out first");
        }
    }

    public void assertInGame() throws Exception{
        if (state != State.Game){
            throw new Exception("User must join game first");
        }
    }







    private void updateGameList(Collection<GameData> gameList){
        this.gameDataList.clear();
        for (GameData gameData : gameList) {
            this.gameDataList.add(gameData);
        }
    }

    private void displayGameList(){
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print("Listing Games: \n");
        for (int i = 1; i < gameDataList.size()+1; i++){
            GameData gameData = gameDataList.get(i-1);
            System.out.print(SET_TEXT_COLOR_GREEN);
            System.out.print(SET_TEXT_BOLD);
            System.out.print("  Game: " + i + "\n");
            System.out.print(RESET_TEXT_BOLD_FAINT);
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.print("      Game Name: " +  gameData.gameName() + "\n");
            String wUsername = gameData.whiteUsername();
            String bUsername = gameData.blackUsername();
            if (wUsername == null){
                wUsername = "No player added";
            }
            if (bUsername == null){
                bUsername = "No player added";
            }
            System.out.print("      White Player: " +  wUsername + "\n");
            System.out.print("      Black Player: " +  bUsername + "\n");
        }
    }


    private ChessGame.TeamColor convertColor(String color) throws Exception{
        if (color == null || color == "" || color == "blank"){
            throw new ResponseException(500, "HTTP Request needs to be made first");
        }
        else if (color.equals("WHITE")){
            return ChessGame.TeamColor.WHITE;
        }
        else if (color.equals("BLACK")) {
            return ChessGame.TeamColor.BLACK;
        }
        else {
            throw new ResponseException(500, "Invalid Team Color");
        }
    }




}
