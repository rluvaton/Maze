package Maze;

/**
 * Directions To Move
 * The Values are those because if I wanna know where to go in matrix I would know
 *
 * @see Helpers.Utils.getHorizontalDirection
 * @see Helpers.Utils.getVerticalDirection
 *
 * @example
 * I Wanna Go to top and I at 3, 4
 * So I get -1
 * For Vertical I do -1 / 1 = -1 -> 3 + (-1) = 2
 * For Horizontal I do (int)(-1 / 2) = 0 -> 4 + 0 = 4
 *
 */
public enum Direction
{
    TOP(-1),
    RIGHT(2),
    BOTTOM(1),
    LEFT(-2);

    private final int value;

    Direction(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}