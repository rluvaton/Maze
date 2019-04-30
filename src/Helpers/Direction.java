package Helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Directions To Move
 * The Values are those because if I wanna know where to go in matrix I would kno
 *
 * @example I Wanna Go to top and I at 3, 4
 * So I get -1
 * For Vertical I do -1 / 1 = -1 -> 3 + (-1) = 2
 * For Horizontal I do (int)(-1 / 2) = 0 -> 4 + 0 = 4
 */
public enum Direction {
    TOP(-1),
    RIGHT(2),
    BOTTOM(1),
    LEFT(-2);

    private final int value;
    public static Map<Direction, Direction> OPPOSITE_DIRECTIONS = Direction.createOppositeDirectionsMap();

    Direction(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

    private static Map<Direction, Direction> createOppositeDirectionsMap() {
        HashMap<Direction, Direction> directions = new HashMap<>();

        directions.put(Direction.TOP, Direction.BOTTOM);
        directions.put(Direction.RIGHT, Direction.LEFT);
        directions.put(Direction.BOTTOM, Direction.TOP);
        directions.put(Direction.LEFT, Direction.RIGHT);

        return directions;
    }

    public static Direction getOppositeDirection(Direction direction) {
        return Direction.OPPOSITE_DIRECTIONS.get(direction);
    }

}