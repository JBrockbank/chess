package chess.pieces;

import chess.*;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{

    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor){
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        if(this.teamColor == ChessGame.TeamColor.WHITE){
            if (CanMoveHere(row + 1, col)){
                AddMove(row + 1, col);
                if(row == 2) {
                    if (CanMoveHere(row + 2, col)) {
                        AddMove(row + 2, col);
                    }
                }
            }
            if (IsEnemyThere(row + 1, col - 1)) {
                AddMove(row+1, col-1);
            }
            if (IsEnemyThere(row + 1, col + 1)) {
                AddMove(row + 1, col + 1);
            }
        }
        else {
            if (CanMoveHere(row - 1, col)){
                AddMove(row - 1, col);
                if(row == 7) {
                    if (CanMoveHere(row - 2, col)) {
                        AddMove(row - 2, col);
                    }
                }
            }
            if (IsEnemyThere(row - 1, col - 1)) {
                AddMove(row - 1, col - 1);
            }
            if (IsEnemyThere(row - 1, col + 1)) {
                AddMove(row - 1, col + 1);
            }
        }

        return validMoves;
    }

    public boolean CanMoveHere(int row, int col) {
        if (row >= 1 && row < 9 && col >= 1 && col < 9){
            ChessPosition pos = new ChessPosition(row, col);
            if(chessBoard.getPiece(pos) == null) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public void AddMove(int row, int col){
        if(row == 1 || row == 8){
            PromotionAddMove(row, col);
        }
        else {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(position, endPosition, null);
            validMoves.add(move);
        }
    }

    public void PromotionAddMove(int row, int col){
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove Queen = new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN);
        ChessMove Rook = new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK);
        ChessMove Bishop = new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP);
        ChessMove Knight = new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT);
        validMoves.add(Queen);
        validMoves.add(Rook);
        validMoves.add(Bishop);
        validMoves.add(Knight);
    }

    public boolean IsEnemyThere(int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = chessBoard.getPiece(pos);
        if(piece == null) {
            return false;
        }
        else return piece.getTeamColor() != this.teamColor;
    }


}