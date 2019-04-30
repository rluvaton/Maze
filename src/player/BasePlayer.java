package player;

import Helpers.Coordinate;
import Helpers.Direction;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import player.exceptions.PlayerNotRunning;

import static Helpers.Utils.Instance;

/**
 * Base Player
 * Abstract class for player
 */
public class BasePlayer
{

    // region Variables

    /**
     * Player name
     */
    private String name;

    /**
     * Subject for where the player move
     */
    private BehaviorSubject<Direction> playerMoveSub = BehaviorSubject.create();

    /**
     * Subject for where the player move
     */
    private BehaviorSubject<LocationChanged> playerLocationChangedSub = BehaviorSubject.create();

    /**
     * Current Location
     */
    private Coordinate location;

    /**
     * Previous Location
     */
    private Coordinate prevLocation;

    /**
     * Time left to user
     */
    private int time;

    /**
     * Points of the user
     */
    private int points = 0;

    private static int count = 1;

    // endregion

    /**
     * Constructor
     *
     * @param location Starting location of the player
     */
    public BasePlayer(Coordinate location) {
        this.location = location;
        this.name = "Player " + count++;
    }

    /**
     * Constructor
     *
     * @param location Starting location of the player
     * @param name     Player name
     */
    public BasePlayer(Coordinate location, String name) {
        this.name = name;
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
     *
     * @param direction Moving Direction
     */
    protected void notifyMoved(Direction direction) {
        this.playerMoveSub.onNext(direction);
    }

    /**
     * Move Top
     */
    public void top() {
        this.notifyMoved(Direction.TOP);
    }

    /**
     * Move Right
     */
    public void right() {
        this.notifyMoved(Direction.RIGHT);
    }

    /**
     * Move Bottom
     */
    public void bottom() {
        this.notifyMoved(Direction.BOTTOM);
    }

    /**
     * Move Left
     */
    public void left() {
        this.notifyMoved(Direction.LEFT);
    }

    public void onPlayerTeleported() throws PlayerNotRunning {

    }

    // region Getter & Setter

    /**
     * Get the observables of the player moves
     *
     * @return Observable of the moving player
     */
    public Observable<Direction> getPlayerMoveObs() {
        return this.playerMoveSub;
    }

    /**
     * Get the observable of the player location
     *
     * @return Observable of the location player
     */
    public Observable<LocationChanged> getPlayerLocationChangedObs() {
        return this.playerLocationChangedSub;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        prevLocation = this.location;

        // Notify of the location change
        this.playerLocationChangedSub.onNext(new LocationChanged(this.location, location));

        this.location = location;
    }

    public void setLocation(Direction direction) {
        Coordinate nextLocation = Instance.getNextLocation(this.location, direction);

        prevLocation = this.location;

        // Notify of the location change
        this.playerLocationChangedSub.onNext(new LocationChanged(this.location, nextLocation, direction));

        this.location = nextLocation;
    }

    public Coordinate getPrevLocation() {
        return prevLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // region Points

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
    }

    // endregion

    // region Time

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addTime(int time) {
        this.time += time;
    }

    public void removeTime(int time) {
        this.time -= time;
    }

    // endregion

    // endregion
}
