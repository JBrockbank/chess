package chess.pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

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
            if (CanMoveHere(row + i, col + i) && (i + row < 9) && (i + col < 9)) {
                AddMove(row + i, col + i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (CanMoveHere(row + i, col - i) && (i + row < 9) && (col - i > 0)) {
                AddMove(row + i, col - i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (CanMoveHere(row - i, col + i) && (row - i > 0) && (col + i < 9)) {
                AddMove(row - i, col + i);
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (CanMoveHere(row - i, col - i) && (row - i > 0) && (col - i > 0)) {
                AddMove(row - i, col - i);
            } else {
                break;
            }
        }
        return validMoves;
    }
}

