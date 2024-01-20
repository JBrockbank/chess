package chess.pieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator {

    private Collection<ChessMove> validMoves = new HashSet<ChessMove>();
    private ChessPosition position;
    private ChessBoard chessBoard;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        setPosition(position);
        chessBoard = board;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();
        for (int i = 1; i < 8; i++) {
            if (CanMoveHere(row + i, col + i) && (i + row < 8) && (i + col < 8)) {
                AddMove(row + i, col + i);
            }
            if (CanMoveHere(row + i, col - i) && (i + row < 8) && (col - i > 0)) {
                AddMove(row + i, col - i);
            }
            if (CanMoveHere(row - i, col + i) && (row - i > 0) && (col + i < 8)) {
                AddMove(row - i, col + i);
            }
            if (CanMoveHere(row - i, col - i) && (row - i > 0) && (col - i > 0)) {
                AddMove(row - i, col - i);
            }

        }
        return validMoves;
    }

    public ChessPosition getPosition() {
        return position;
    }

    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    public boolean CanMoveHere(int row, int col) {
        if (row > 0 || row < 8 || col > 0 || col < 8){
            return true;
        }
        else {
            return false;
        }
    }

    public ChessPosition CreatePosition(int row, int col) {
        ChessPosition newPosition = new ChessPosition(row, col);
        return newPosition;
    }

    public void AddMove(int row, int col){
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove move = new ChessMove(position, endPosition, null);
        validMoves.add(move);
    }

}

