package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d) to (%d,%d).", startPosition.getRow(), startPosition.getColumn(), endPosition.getRow(), endPosition.getColumn());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(this == obj) {
            return true;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        ChessMove other = (ChessMove) obj;
        return this.startPosition.equals(other.startPosition) && this.endPosition.equals(other.endPosition) && this.promotionPiece.equals(other.promotionPiece);
    }


    @Override
    public int hashCode() {
        return startPosition.hashCode() + endPosition.hashCode();
    }
}
