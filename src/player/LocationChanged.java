package player;

import Helpers.Direction;
import Helpers.Tuple;

/**
 * Location Changed Event Class
 */
public class LocationChanged {
    /**
     * From location
     */
    public Tuple<Integer, Integer> from;

    /**
     * To location
     */
    public Tuple<Integer, Integer> to;

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
    public LocationChanged(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Constructor
     * @param from from which location it moved
     * @param to To what location it changed
     * @param direction To which direction it changed
     */
    public LocationChanged(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, Direction direction) {
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    // endregion
}
