package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class PieceMovesCalculator {
    public Collection<ChessMove> validMoves = new HashSet<ChessMove>();
    public ChessPosition position;
    public ChessBoard chessBoard;
    public ChessGame.TeamColor teamColor;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor){
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public PieceMovesCalculator() {
    }

    public Collection<ChessMove> pieceMoves() {
        return null;
    }


    public ChessPosition getPosition() {
        return position;
    }

    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    public void AddMove(int row, int col){
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove move = new ChessMove(position, endPosition, null);
        validMoves.add(move);
    }

    public boolean CanMoveHere(int row, int col) {
        if (row >= 1 && row < 9 && col >= 1 && col < 9){
            ChessPosition pos = new ChessPosition(row, col);
            if(chessBoard.getPiece(pos) == null){
                return true;
            } else if(chessBoard.getPiece(pos).getTeamColor() == this.teamColor) {
                return false;
            }
            else {
                AddMove(row, col);
                return false;
            }
        }
        else {
            return false;
        }
    }
}
