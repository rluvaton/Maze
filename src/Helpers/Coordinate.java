package Helpers;

public class Coordinate {
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

    public boolean equals(Coordinate coordinate) {
        return coordinate != null && this.row == coordinate.row && this.column == coordinate.column;
    }

    public static boolean equals(Coordinate coordinate1, Coordinate coordinate2) {
        return (coordinate1 == null && coordinate2 == null) ||
                (coordinate1 != null && coordinate1.equals(coordinate2));
    }

    @Override
    public Coordinate clone() {
        return new Coordinate(this.row, this.column);
    }

    protected static Coordinate clone(Coordinate coordinate) {
        return coordinate == null ? null : new Coordinate(coordinate.row, coordinate.column);
    }
}
