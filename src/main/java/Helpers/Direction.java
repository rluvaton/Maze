package Helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Directions To Move
 * The values are the number to add when you go to that direction
 * You can use {@link Utils#moveCoordinatesToDirection(Coordinate, Direction)} for moving in direction
 */
public enum Direction {
    UP(new Coordinate(-1, 0), (short) 90),
    RIGHT(new Coordinate(0, 1), (short) 0),
    DOWN(new Coordinate(1, 0), (short) 270),
    LEFT(new Coordinate(0, -1), (short) 180);

    private final Coordinate value;
    private final short angle;
    public static Map<Direction, Direction> OPPOSITE_DIRECTIONS = Direction.createOppositeDirectionsMap();

    Direction(final Coordinate value, final short angle) {
        this.value = value;
        this.angle = angle;
    }

    public Coordinate getValue() {
        // We clone because we don't want the value to change somehow
        return value.clone();
    }

    public short getAngle() {
        return this.angle;
    }

    private static Map<Direction, Direction> createOppositeDirectionsMap() {
        HashMap<Direction, Direction> directions = new HashMap<>();

        directions.put(Direction.UP, Direction.DOWN);
        directions.put(Direction.RIGHT, Direction.LEFT);
        directions.put(Direction.DOWN, Direction.UP);
        directions.put(Direction.LEFT, Direction.RIGHT);

        return directions;
    }

    public static Direction getOppositeDirection(Direction direction) {
        return Direction.OPPOSITE_DIRECTIONS.get(direction);
    }

}