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

        //Adding Moves for White Pawns
        if(this.teamColor == ChessGame.TeamColor.WHITE){
            if (canMoveHere(row + 1, col)){
                addMove(row + 1, col);
                if(row == 2) {
                    if (canMoveHere(row + 2, col)) {
                        addMove(row + 2, col);
                    }
                }
            }
            if (isEnemyThere(row + 1, col - 1)) {
                addMove(row+1, col-1);
            }
            if (isEnemyThere(row + 1, col + 1)) {
                addMove(row + 1, col + 1);
            }
        }
        //Adding Moves for Black Pawns
        else {
            if (canMoveHere(row - 1, col)){
                addMove(row - 1, col);
                if(row == 7) {
                    if (canMoveHere(row - 2, col)) {
                        addMove(row - 2, col);
                    }
                }
            }
            if (isEnemyThere(row - 1, col - 1)) {
                addMove(row - 1, col - 1);
            }
            if (isEnemyThere(row - 1, col + 1)) {
                addMove(row - 1, col + 1);
            }
        }
        return validMoves;
    }

    public boolean canMoveHere(int row, int col) {
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

    public void addMove(int row, int col){
        if(row == 1 || row == 8){
            promotionAddMove(row, col);
        }
        else {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(position, endPosition, null);
            validMoves.add(move);
        }
    }

    public void promotionAddMove(int row, int col){
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove queen = new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN);
        ChessMove rook = new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK);
        ChessMove bishop = new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP);
        ChessMove knight = new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT);
        validMoves.add(queen);
        validMoves.add(rook);
        validMoves.add(bishop);
        validMoves.add(knight);
    }

    public boolean isEnemyThere(int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = chessBoard.getPiece(pos);
        if(piece == null) {
            return false;
        }
        else return piece.getTeamColor() != this.teamColor;
    }


}