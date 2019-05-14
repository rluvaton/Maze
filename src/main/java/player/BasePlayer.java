package player;

import GUI.Color;
import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.RandomHelper;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.exceptions.InvalidDirectionException;
import player.exceptions.PlayerNotRunning;

import java.util.HashMap;
import java.util.Map;

import static Helpers.Utils.Instance;

/**
 * Base Player
 * Abstract class for player
 */
public abstract class BasePlayer {
    // region Variables

    /**
     * Pause Players Subject
     */
    protected static Subject<Boolean> onPauseSub = PublishSubject.create();

    /**
     * Player name
     */
    private String name;

    protected Subject<Boolean> onFinish = PublishSubject.create();

    /**
     * Subject for where the player move
     */
    private BehaviorSubject<Direction> playerMoveSub = BehaviorSubject.create();

    /**
     * Subject for where the player move
     */
    private BehaviorSubject<LocationChanged> playerLocationChangedSub = BehaviorSubject.create();

    protected Subject<Boolean> onPlayerPauseSub = PublishSubject.create();

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

    private final Map<Direction, Runnable> directionActions = this.createDirectionActions();

    private Color color = RandomHelper.generateItemFromArray(Color.values());

    // endregion

    // Constructor that called in before the Constructor
    {
        listenToOnPause();
    }

    private void listenToOnPause() {
        onPauseSub.subscribe(this::onPauseAction);
    }

    protected void onPauseAction(Boolean isPause) {
        try {
            if (isPause) {
                this.pause();
            } else {
                this.resume();
            }
        } catch (PlayerNotRunning playerNotRunning) {
            playerNotRunning.printStackTrace();
        }
    }

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

    public BasePlayer(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public BasePlayer(Coordinate location, String name, Color color) {
        this(location, name);
        this.color = color;
    }

    /**
     * Create Action for each direction
     *
     * @return Map with all the directions and the actions
     */
    private Map<Direction, Runnable> createDirectionActions() {
        Map<Direction, Runnable> keyAssignment = new HashMap<>();

        keyAssignment.put(Direction.UP, this::up);
        keyAssignment.put(Direction.DOWN, this::down);
        keyAssignment.put(Direction.RIGHT, this::right);
        keyAssignment.put(Direction.LEFT, this::left);

        return keyAssignment;
    }

    /**
     * Move at direction
     *
     * @param direction direction to move
     */
    public void move(Direction direction) throws InvalidDirectionException {
        if (!this.directionActions.containsKey(direction)) {
            throw new InvalidDirectionException(direction);
        }

        this.directionActions.get(direction).run();
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
    public void up() {
        this.notifyMoved(Direction.UP);
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
    public void down() {
        this.notifyMoved(Direction.DOWN);
    }

    /**
     * Move Left
     */
    public void left() {
        this.notifyMoved(Direction.LEFT);
    }

    public void onPlayerTeleported() throws PlayerNotRunning {

    }

    public void onPlayerFinished() {
        this.onFinish.onNext(true);
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

    public Observable<Boolean> getOnPlayerPauseObs() {
        return onPlayerPauseSub;
    }

    public static Observable<Boolean> getOnPauseObs() {
        return onPauseSub;
    }

    public static void pauseAllPlayers() throws PlayerNotRunning {
        onPauseSub.onNext(true);
    }

    public static void resumeAllPlayers() throws PlayerNotRunning {
        onPauseSub.onNext(false);
    }

    public abstract void pause() throws PlayerNotRunning;

    public abstract void resume() throws PlayerNotRunning;

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
        Coordinate nextLocation = Instance.moveCoordinatesToDirection(this.location, direction);

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

    public Color getColor() {
        return color;
    }


    // endregion
}
