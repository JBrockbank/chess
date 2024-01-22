package chess;

import chess.pieces.*;


import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */

    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (pieceType == PieceType.BISHOP) {
            BishopMovesCalculator pieceCalculator = new BishopMovesCalculator(board, myPosition, pieceColor);
            Collection<ChessMove> validMoves =  pieceCalculator.pieceMoves();
            return validMoves;
        }
        else if (pieceType == PieceType.KING) {
            KingMovesCalculator pieceCalculator = new KingMovesCalculator(board, myPosition, pieceColor);
            Collection<ChessMove> validMoves =  pieceCalculator.pieceMoves();
            return validMoves;
        }
        else if (pieceType == PieceType.ROOK) {
            RookMovesCalculator pieceCalculator = new RookMovesCalculator(board, myPosition, pieceColor);
            Collection<ChessMove> validMoves =  pieceCalculator.pieceMoves();
            return validMoves;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }
}

