package chess.pieces;

import chess.*;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {



    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        setPosition(position);
        chessBoard = board;
        teamColor = pieceColor;
    }

    public Collection<ChessMove> pieceMoves() {
        ChessPosition position = getPosition();
        int row = position.getRow();
        int col = position.getColumn();

        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row + i, col + i) && (i + row < 9) && (i + col < 9)) {
                addMove(row + i, col + i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row + i, col - i) && (i + row < 9) && (col - i > 0)) {
                addMove(row + i, col - i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row - i, col + i) && (row - i > 0) && (col + i < 9)) {
                addMove(row - i, col + i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (canMoveHere(row - i, col - i) && (row - i > 0) && (col - i > 0)) {
                addMove(row - i, col - i);
            } else {
                break;
            }
        }
        return validMoves;
    }
}

