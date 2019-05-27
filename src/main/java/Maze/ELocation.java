package Maze;

import Helpers.Coordinate;
import Helpers.Direction;

/**
 * Exit or Entrance Location
 */
public class ELocation {
    /**
     * The location of the exit / enter
     */
    private Coordinate location;

    /**
     * The direction to go to, for exiting / entering
     */
    private Direction direction;

    /**
     * ELocation Type, Enter / Exit
     */
    private ELocationType type;

    public ELocation(Coordinate location, Direction direction, ELocationType type) {
        this.location = location;
        this.direction = direction;
        this.type = type;
    }

    /**
     * Check if location & direction are entrance / exit location
     *
     * @param location  Location to check
     * @param direction Direction to check
     * @return Returns if the combination of the location and direction is entrance / exit location
     */
    public boolean isAtELocation(Coordinate location, Direction direction) {
        return Coordinate.equals(this.location, location) && this.direction == direction;
    }

    // region Getters & Setter

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ELocationType getType() {
        return type;
    }

    public void setType(ELocationType type) {
        this.type = type;
    }

    // endregion
}
