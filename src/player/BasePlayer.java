package player;

import Helpers.Direction;
import Helpers.Tuple;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static Helpers.Utils.Instance;

/**
 * Base Player
 * Abstract class for player
 */
public abstract class BasePlayer
{
    /**
     * Subject for where the player move
     */
    private BehaviorSubject<Direction> playerMoveSub = BehaviorSubject.create();

    /**
     * Current Location
     */
    private Tuple<Integer, Integer> location;

    /**
     * Constructor
     *
     * @param location Starting location of the player
     */
    public BasePlayer(Tuple<Integer, Integer> location) {
        this.location = location;
    }

    /**
     * Move at direction
     *
     * @param direction direction to move
     */
    public void move(Direction direction) {
        switch (direction) {
            case TOP:
                this.top();
                break;
            case RIGHT:
                this.right();
                break;
            case BOTTOM:
                this.bottom();
                break;
            case LEFT:
                this.left();
                break;
            default:
                System.out.println("Direction not recognized: " + direction);
                break;
        }
    }

    /**
     * Notify that the player moved
     * @param direction Moving Direction
     */
    protected void notifyMoved(Direction direction) {
        this.playerMoveSub.onNext(direction);
    }

    /**
     * Move Top
     */
    public abstract void top();

    /**
     * Move Right
     */
    public abstract void right();

    /**
     * Move Bottom
     */
    public abstract void bottom();

    /**
     * Move Left
     */
    public abstract void left();

    // region Getter & Setter

    /**
     * Get the player move observable
     * @return Observable of the moving player
     */
    public Observable<Direction> getPlayerMoveObs() {
        return this.playerMoveSub;
    }

    public Tuple<Integer, Integer> getLocation() {
        return location;
    }

    public void setLocation(Tuple<Integer, Integer> location) {
        this.location = location;
    }

    public void setLocation(Direction direction) {
        this.location = Instance.getNextCell(this.location, direction);
    }

    // endregion
}
