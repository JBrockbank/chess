package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor){
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row + i, col) && (i + row < 9)){
                addMove(row + i, col);
            } else{
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row, col + i) && (i + col < 9)){
                addMove(row, col + i);
            } else{
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row - i, col) && (row - i > 0)){
                addMove(row - i, col);
            } else{
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row, col - i) && (col - i > 0)){
                addMove(row, col - i);
            } else{
                break;
            }
        }
        return validMoves;

    }
}
