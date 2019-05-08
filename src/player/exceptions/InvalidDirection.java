package player.exceptions;

import Helpers.Direction;

public class InvalidDirection extends Exception {
    private Direction direction;

    public InvalidDirection(Direction direction) {
        super("Invalid direction , " + (direction == null ? "null" : direction.toString()));
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
