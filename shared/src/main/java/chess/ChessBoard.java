package chess;

import java.lang.reflect.Array;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        ChessPiece.PieceType[][] board = new ChessPiece.PieceType[8][8];

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
            }
        }

        for (int i = 0; i < 8; i++) {
            board[0][i] = 'p';
            board[8][i] = 'p';
        }
        for (int i = 0; i < 8; i++) {

        }

    }
}
