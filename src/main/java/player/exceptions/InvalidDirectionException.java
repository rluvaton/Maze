package player.exceptions;

import Helpers.Direction;

public class InvalidDirectionException extends Exception {
    private Direction direction;

    public InvalidDirectionException(Direction direction) {
        super("Invalid direction , " + (direction == null ? "null" : direction.toString()));
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
