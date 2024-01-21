package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator {

    private Collection<ChessMove> validMoves = new HashSet<ChessMove>();
    private ChessPosition position;
    private ChessBoard chessBoard;
    private ChessGame.TeamColor teamColor;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        for (int i = 0; i < 8; i++) {
            if (CanMoveHere(row + i, col + i) && (i + row < 9) && (i + col < 9)) {
                AddMove(row + i, col + i);
            }
            if (CanMoveHere(row + i, col - i) && (i + row < 9) && (col - i > 0)) {
                AddMove(row + i, col - i);
            }
            if (CanMoveHere(row - i, col + i) && (row - i > 0) && (col + i < 9)) {
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
        if (row >= 1 && row < 9 && col >= 1 && col < 9){
            ChessPosition pos = new ChessPosition(row, col);
            if(chessBoard.getPiece(pos) == null){
                return true;
            }
            if(chessBoard.getPiece(pos).getTeamColor() == this.teamColor) {
                return false;
            }
            else {
                return true;
            }
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

