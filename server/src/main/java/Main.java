import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server s = new Server();
        s.run(8080);
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("Server Running");
    }
}

