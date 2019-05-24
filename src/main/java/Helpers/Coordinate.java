package Helpers;

import java.util.Objects;

public class Coordinate implements SuccessCloneable<Coordinate> {
    private int row;
    private int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return row == that.row &&
                column == that.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public static boolean equals(Coordinate coordinate1, Coordinate coordinate2) {
        return (coordinate1 == null && coordinate2 == null) ||
                (coordinate1 != null && coordinate1.equals(coordinate2));
    }



    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Coordinate clone() {
        return new Coordinate(this.row, this.column);
    }

    protected static Coordinate clone(Coordinate coordinate) {
        return coordinate == null ? null : new Coordinate(coordinate.row, coordinate.column);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
