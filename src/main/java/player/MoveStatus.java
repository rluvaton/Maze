package player;

/**
 * The Status of movement request
 */
public enum MoveStatus {

    /**
     * Move isn't valid
     */
    INVALID_MOVE,

    /**
     * Move valid
     */
    VALID,

    /**
     * Moved to finish location
     */
    FINISHED;

    public static MoveStatus getValidationMoveFromBoolean(boolean isValidMove) {
        return isValidMove ? MoveStatus.VALID : MoveStatus.INVALID_MOVE;
    }
}
