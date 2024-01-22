package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor){
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        KingMovesCalculator kingMoves = new KingMovesCalculator(chessBoard, position, teamColor);
        RookMovesCalculator rookMoves = new RookMovesCalculator(chessBoard, position, teamColor);
        BishopMovesCalculator bishopMoves = new BishopMovesCalculator(chessBoard, position, teamColor);

        validMoves.addAll(kingMoves.pieceMoves());
        validMoves.addAll(rookMoves.pieceMoves());
        validMoves.addAll(bishopMoves.pieceMoves());

        return validMoves;
    }

}
