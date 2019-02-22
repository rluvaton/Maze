package player;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;

/**
 * Location Changed Event Class
 */
public class LocationChanged {
    /**
     * From location
     */
    public Coordinate from;

    /**
     * To location
     */
    public Coordinate to;

    /**
     * Direction to move
     */
    public Direction direction;

    // region Constructors

    /**
     * Constructor
     * @param from from which location it moved
     * @param to To what location it changed
     */
    public LocationChanged(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Constructor
     * @param from from which location it moved
     * @param to To what location it changed
     * @param direction To which direction it changed
     */
    public LocationChanged(Coordinate from, Coordinate to, Direction direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    // endregion
}
