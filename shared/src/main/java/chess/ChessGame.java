package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public TeamColor turnColor;
    public ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.turnColor = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
//        else if (board.getPiece(startPosition).getTeamColor() != getTeamTurn()){
//            return null;
//        }
        else {
            ChessPiece piece = board.getPiece(startPosition);
            Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
            Collection<ChessMove> validMovesCopy =  new HashSet<ChessMove>();
            for (ChessMove move: validMoves){
                validMovesCopy.add(move);
            }
            ChessGame testGame = new ChessGame();
            ChessBoard boardCopy = board.deepCopy();
            testGame.setBoard(boardCopy);

            for (ChessMove move : validMovesCopy){
                ChessPosition oldPiecePos = move.getEndPosition();
                ChessPiece oldPiece = testGame.board.getPiece(oldPiecePos);
                testGame.testMakeMove(move);
                ChessGame.TeamColor pieceColor = piece.getTeamColor();
                if (testGame.isInCheck(pieceColor)){
                    validMoves.remove(move);
                }
                testGame.undoMove(move, oldPiece);
            }
            return validMoves;
        }

    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        System.out.println("Making Move: ");
        System.out.println(move.toString());
        ChessPosition pos = move.getStartPosition();
        ChessGame.TeamColor pieceColor = board.getPiece(pos).getTeamColor();

        if (pieceColor != turnColor){
            throw new InvalidMoveException("Invalid Move");
        }

        if (outOfBounds(move)){
            throw new InvalidMoveException("Invalid Move - Out of Bounds");
        }
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        if (validMoves != null && validMoves.contains(move)){
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            ChessPiece newPiece = board.getPiece(startPos);
            if (promotionPiece != null){
                ChessPiece PromoPiece = new ChessPiece(newPiece.getTeamColor(), promotionPiece);
                newPiece = PromoPiece;
            }

            board.addPiece(endPos, newPiece);
            board.addPiece(startPos, null);
            if (turnColor == TeamColor.WHITE){
                setTeamTurn(TeamColor.BLACK);
            }
            else {
                setTeamTurn(TeamColor.WHITE);
            }
        }
        else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean inCheck = false;
        ChessPosition kingPos = getKingPos(teamColor);
        Collection<ChessMove> opposingMoves = new HashSet<ChessMove>();

        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece == null){
                }
                else if (piece.getTeamColor() == teamColor){
                }
                else {
                    opposingMoves = piece.pieceMoves(getBoard(), pos);
                    for (ChessMove move : opposingMoves){
                        ChessPosition endPos = move.getEndPosition();
                        if (endPos.equals(kingPos)){
                            inCheck = true;
                            break;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = new HashSet<ChessMove>();
        if (isInCheck(teamColor)){
            for (int row = 1; row < 9; row++) {
                for (int col = 1; col < 9; col++) {
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);
                    if (piece == null){
                    }
                    else if (piece.getTeamColor() != teamColor){
                    }
                    else {
                        teamMoves.addAll(piece.pieceMoves(board, pos));
                    }
                }
            }
            for (ChessMove move : teamMoves){
                ChessPiece oldPiece = board.getPiece(move.getEndPosition());
                testMakeMove(move);
                if (!isInCheck(teamColor)){
                    undoMove(move, oldPiece);
                    return false;
                }
                undoMove(move, oldPiece);
            }
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = new HashSet<ChessMove>();
        if (!isInCheck(teamColor)){
            for (int row = 1; row < 9; row++){
                for (int col = 1; col < 9; col++){
                    ChessPosition pos = new ChessPosition(row, col);
                    ChessPiece piece = board.getPiece(pos);
                    if (piece == null){
                    }
                    else if (piece.getTeamColor() != teamColor){
                    }
                    else {
                        teamMoves.addAll(validMoves(pos));
                    }
                }
            }

        }
        else {
            return false;
        }
        if (teamMoves.size() == 0){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public ChessPosition getKingPos(TeamColor color){
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece == null){
                }
                else if (piece.getTeamColor() != color){
                }
                else {
                    if (piece.getPieceType() == ChessPiece.PieceType.KING){
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    public void testMakeMove(ChessMove move) {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece newPiece = board.getPiece(startPos);
        board.addPiece(endPos, newPiece);
        board.addPiece(startPos, null);
    }

    public void undoMove(ChessMove move, ChessPiece oldPiece){
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece newPiece = board.getPiece(endPos);
        board.addPiece(startPos, newPiece);
        board.addPiece(endPos, oldPiece);
    }

    public boolean outOfBounds(ChessMove move){
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();
        if (startRow < 1 || startRow > 8) {
            return true;
        }
        else if (startCol < 1 || startCol > 8) {
            return true;
        }
        else if (endRow < 1 || endRow > 8) {
            return true;
        }
        else if (endCol < 1 || endCol > 8) {
            return true;
        }
        else {
            return false;
        }
    }

}
