package Maze;

import Helpers.Coordinate;
import Helpers.Direction;

public class ELocationCell extends Cell {


    public ELocationCell(Cell cell, Direction direction, ELocationType type) {
        super(cell);
        this.direction = direction;
        this.type = type;
    }

    public ELocationCell(int row, int col, Direction direction, ELocationType type) {
        super(row, col);
        this.direction = direction;
        this.type = type;
    }

    public ELocationCell(Coordinate position, Direction direction, ELocationType type) {
        super(position);
        this.direction = direction;
        this.type = type;
    }

    /**
     * The direction to go to, for exiting / entering
     */
    private Direction direction;

    /**
     * ELocation Type, Enter / Exit
     */
    private ELocationType type;

    /**
     * Check if location & direction are entrance / exit location
     *
     * @param location  Location to check
     * @param direction Direction to check
     * @return Returns if the combination of the location and direction is entrance / exit location
     */
    public boolean isAtELocation(Coordinate location, Direction direction) {
        return Coordinate.equals(super.location, location) && this.direction == direction;
    }

    public ELocationType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }
}
