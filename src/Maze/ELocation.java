package Maze;

import Helpers.Direction;
import Helpers.Tuple;

/**
 * Exit or Entrance Location
 */
public class ELocation {
    /**
     * The location of the exit / enter
     */
    private Tuple<Integer, Integer> location;

    /**
     * The direction to go to, for exiting / entering
     */
    private Direction direction;

    /**
     * ELocation Type, Enter / Exit
     */
    private ELocationType type;

    public ELocation(Tuple<Integer, Integer> location, Direction direction, ELocationType type) {
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
    public boolean isAtELocation(Tuple<Integer, Integer> location, Direction direction) {
        return !(location == null || direction == null || !this.location.item1.equals(location.item1) ||
                 !this.location.item2.equals(location.item2) || this.direction != direction);
    }

    // region Getters & Setter

    public Tuple<Integer, Integer> getLocation() {
        return location;
    }

    public void setLocation(Tuple<Integer, Integer> location) {
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
