package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

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
            String gameIndex = params[0];
            String playerColor = params[1];
            int ID = 0;
            ID = Integer.parseInt(gameIndex);
            if (ID > gameDataList.size()){
                throw new Exception("Game doesn't exist");
            }
            int gameID = gameDataList.get(ID-1).gameID();
            playerColor = playerColor.toUpperCase();
            GameData gameData = server.joinGame(gameID, playerColor);
            displayGame(gameData);
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
            int gameID = gameDataList.get(ID-1).gameID();
            GameData gameData = server.observeGame(gameID);
            displayGame(gameData);
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



    public void displayGame(GameData gameData){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        var output = gameData.gameName() + ":\n";
        String wUsername = gameData.whiteUsername();
        String bUsername = gameData.blackUsername();
        if (wUsername == null){
            wUsername = "No player added";
        }
        if (bUsername == null){
            bUsername = "No player added";
        }
        output += "White Player: " + wUsername + "\n";
        output += "Black Player: " + bUsername + "\n";
        if (gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE){
            output += wUsername + "'s turn to move";
        }
        else {
            output += bUsername + "'s turn to move\n\n";
        }
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(output);
        out.print("\n");
        drawBoard(out, gameData.game());
        drawBoardBlack(out, gameData.game());

    }


    private static void drawBoard(PrintStream out, ChessGame game) {
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("  A ");
        out.print(" B ");
        out.print(" C ");
        out.print(" D ");
        out.print(" E ");
        out.print(" F ");
        out.print(" G ");
        out.print(" H \n");
        for (int j = 1; j < 9; j++){
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(j);
            for (int i = 1; i < 9; i++) {
                if ((j % 2) != 0){
                    if ((i % 2) == 0){
                        setYellow(out);
                    }
                    else {
                        setBrown(out);
                    }
                    evalBoard(out, game, j, i);
                }
                else {
                    if ((i % 2) == 0){
                        setBrown(out);
                    }
                    else {
                        setYellow(out);
                    }
                    evalBoard(out, game, j, i);
                }
            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
    }

    private static void drawBoardBlack(PrintStream out, ChessGame game) {
        out.print(RESET_BG_COLOR);
        for (int j = 1; j < 9; j++){
            out.print("\n");
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(9-j);
            for (int i = 1; i < 9; i++) {
                if ((j % 2) != 0){
                    if ((i % 2) == 0){
                        setYellow(out);
                    }
                    else {
                        setBrown(out);
                    }
                    evalBoard(out, game, 9-j, 9-i);
                }
                else {
                    if ((i % 2) == 0){
                        setBrown(out);
                    }
                    else {
                        setYellow(out);
                    }
                    evalBoard(out, game, 9-j, 9-i);
                }
            }
            out.print(RESET_BG_COLOR);
        }
        out.print(SET_TEXT_COLOR_WHITE);
        out.print("\n  H ");
        out.print(" G ");
        out.print(" F ");
        out.print(" E ");
        out.print(" D ");
        out.print(" C ");
        out.print(" B ");
        out.print(" A ");
        out.print("\n");
    }



    public static void evalBoard(PrintStream out, ChessGame game, int row, int col){
        ChessBoard board = game.getBoard();
        ChessPosition pos = new ChessPosition(row, col);
        if (board.getPiece(pos) != null){
            ChessPiece piece = board.getPiece(pos);
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                out.print(SET_TEXT_COLOR_WHITE);
            }
            else {
                out.print(SET_TEXT_COLOR_BLACK);
            }
            out.print(" " + getPieceType(piece) + " ");
        }
        else {
            out.print("   ");
        }
    }

    public static String getPieceType(ChessPiece piece){
        return switch (piece.getPieceType()) {
            case QUEEN -> "Q";
            case KING -> "K";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }

    private static void setBrown(PrintStream out) {
        out.print(SET_BG_COLOR_CREME_BROWN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_CREME_YELLOW);
        out.print(SET_TEXT_COLOR_WHITE);
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


}
