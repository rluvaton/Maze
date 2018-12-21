package player;

/**
 * The Status of movement request
 */
public enum MoveStatus {

    /**
     * Move isn't valid
     */
    NotValidMove,

    /**
     * Move valid
     */
    Valid,

    /**
     * Moved to finish location
     */
    Finished
}
