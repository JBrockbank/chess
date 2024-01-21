package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    public int row;
    public int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null) {
            return false;
        }
        if(this == obj) {
            return true;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        ChessPosition other = (ChessPosition) obj;
        boolean same = this.getRow() == other.getRow() && this.getColumn() == other.getColumn();
        if(same){
            System.out.println("True");
        }
        else {
            System.out.println("False");
        }
        return same;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
