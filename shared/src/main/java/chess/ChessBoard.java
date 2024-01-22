package chess;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[9][9];
    public ChessBoard() {
        System.out.println("Creating the Board");
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();
        System.out.printf("Adding a piece at (%d,%d)\n", row, col);
        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        System.out.printf("Getting a piece at (%d,%d)\n", row, col);
        return board[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Creating and Adding the pawns to the board
        for (int i = 1; i < 9; i++){
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPosition posWhitePawn = new ChessPosition(2, i);
            ChessPosition posBlackPawn = new ChessPosition(7, i);
            addPiece(posWhitePawn, whitePawn);
            addPiece(posBlackPawn, blackPawn);
            }
        int row = 0;
        ChessGame.TeamColor teamColor;
        //Creating and Adding the rest of the non-pawn pieces to the board
        for (int j = 0; j < 2; j++){
            if (j == 0) {
                row = 1;
                teamColor = ChessGame.TeamColor.WHITE;
            }
            else {
                row = 8;
                teamColor = ChessGame.TeamColor.BLACK;
            }
            //Create Chess Pieces of all the types
            ChessPiece rook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            ChessPiece knight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            ChessPiece bishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            ChessPiece queen = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
            //Create the positions for all the pieces
            ChessPosition posRook1 = new ChessPosition(row, 1);
            ChessPosition posRook2 = new ChessPosition(row, 8);
            ChessPosition posKnight1 = new ChessPosition(row, 2);
            ChessPosition posKnight2 = new ChessPosition(row, 7);
            ChessPosition posBishop1 = new ChessPosition(row, 3);
            ChessPosition posBishop2 = new ChessPosition(row, 6);
            ChessPosition posQueen = new ChessPosition(row, 4);
            ChessPosition posKing = new ChessPosition(row, 5);
            //Add the pieces to the board
            addPiece(posRook1, rook);
            addPiece(posRook2, rook);
            addPiece(posKnight1, knight);
            addPiece(posKnight2, knight);
            addPiece(posBishop1, bishop);
            addPiece(posBishop2, bishop);
            addPiece(posQueen, queen);
            addPiece(posKing, king);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
