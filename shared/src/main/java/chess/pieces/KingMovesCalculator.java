package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator{

    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor){
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if (canMoveHere(row + j, col + i)){
                    addMove(row+j,col+i);
                }
            }
        }
        return validMoves;
    }




}
