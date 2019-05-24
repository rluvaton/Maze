package player.ComputerPlayer;

import GUI.Color;
import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Helpers.Utils;
import Maze.Candy.Candy;
import Maze.Candy.CandyPowerType;
import Maze.Cell;
import Maze.ELocation;
import Maze.Maze;
import player.BasePlayer;
import player.exceptions.PlayerAlreadyHaveRunningThreadException;
import player.exceptions.PlayerNotRunning;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static Logger.LoggerManager.logger;

public class ComputerPlayer extends BasePlayer {

    private Maze maze;
    private volatile Cell currentCell;
    private Coordinate endingLocation;
    private Optional<Direction> lastDirection = Optional.empty();

    private Thread playerThread = null;
    private RunnableComputerPlayer runnablePlayer = null;
    private volatile boolean isCurrentlyPlaying = false;
    private AtomicBoolean returnAfterTeleport = new AtomicBoolean(false);

    private int delayMovementInMs = 350;

    /**
     * Human Player Constructor
     *
     * @param location Starting location of the player
     */
    public ComputerPlayer(Coordinate location) {
        super(location);
    }

    public ComputerPlayer(Coordinate location, int delayMovementInMs) {
        super(location);
        this.delayMovementInMs = delayMovementInMs;
    }

    public ComputerPlayer(Coordinate location, String name, int delayMovementInMs) {
        super(location, name);
        this.delayMovementInMs = delayMovementInMs;
    }

    public ComputerPlayer(Coordinate location, String name, Color color, int delayMovementInMs) {
        super(location, name, color);
        this.delayMovementInMs = delayMovementInMs;
    }

    public ComputerPlayer(Coordinate location, String name, Color color) {
        super(location, name, color);
    }

    {
        updateCurrentCellOnPlayerLocationChanged();
    }

    private void updateCurrentCellOnPlayerLocationChanged() {
        this.getPlayerLocationChangedObs()
                .takeUntil(this.onFinish)
                .subscribe(locationChanged -> {
                    if (this.maze != null) {
                        this.currentCell = this.maze.getCell(locationChanged.to);
                    }
                });
    }

    /**
     * Start The player
     *
     * @param maze           Current Maze
     * @param endingLocation The location that this player is heading
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, Coordinate endingLocation) {
        return createRunningThread(maze, endingLocation, null, delayMovementInMs);
    }

    public Thread createRunningThread(Maze maze, Coordinate endingLocation, Direction lastDirection) {
        return createRunningThread(maze, endingLocation, lastDirection, delayMovementInMs);
    }

    public Thread createRunningThread(Maze maze, ELocation exit) {
        ObjectAssertion.requireNonNull(exit, "`exit` can't be null");

        return createRunningThread(maze, exit.getLocation(), exit.getDirection(), delayMovementInMs);
    }


    /**
     * Start The player and the player choose the closest exit
     *
     * @param maze        Current Maze
     * @param stepSpeedMs How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, int stepSpeedMs) {
        return this.createRunningThread(maze, this.findClosestExit(maze), null, stepSpeedMs);
    }


    /**
     * Start The player
     *
     * @param maze           Current Maze
     * @param endingLocation The location that this player is heading
     * @param lastDirection  Last Direction to do
     * @param stepSpeedMs    How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, Coordinate endingLocation, Direction lastDirection, int stepSpeedMs) {
        // TODO - YOU CAN USE LISTENER FOR CANDY COLLECTED OR CANDY DISAPPEARED AND FILTER ONLY TO LOCATION CANDIES
        // TODO - IF CANDY DISAPPEARED THEN RECALCULATE

        this.maze = maze;
        this.endingLocation = endingLocation;
        this.lastDirection = lastDirection == null ? Optional.empty() : Optional.of(lastDirection);

        this.currentCell = this.maze.getCell(this.getLocation());

        List<Direction> steps;
        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (steps.size() == 0) {
            System.out.println("steps length are 0");
        }

        this.isCurrentlyPlaying = true;

        if (this.runnablePlayer != null) {
            this.runnablePlayer.stop();
            this.runnablePlayer = null;
        }

        this.lastDirection.ifPresent(steps::add);

        try {
            this.runnablePlayer = new RunnableComputerPlayer(this, steps, stepSpeedMs);
        } catch (PlayerAlreadyHaveRunningThreadException e) {
            handlePlayerAlreadyHaveRunningThreadException(e);
        }

        this.playerThread = createPlayerThread();
        return this.playerThread;
    }

    private Thread createPlayerThread() {
        Thread playerThread = new Thread(runnablePlayer);
        playerThread.setName("Computer " + this.getName() + " Thread");
        return playerThread;
    }

    @Override
    public void pause() throws PlayerNotRunning {
        if (runnablePlayer == null) {
            throw new PlayerNotRunning();
        }
        this.runnablePlayer.pause();
    }

    @Override
    public void resume() throws PlayerNotRunning {
        if (runnablePlayer == null) {
            throw new PlayerNotRunning();
        }
        this.runnablePlayer.resume();
    }

    @Override
    public void onPlayerTeleported() throws PlayerNotRunning {
        if (returnAfterTeleport.getAcquire()) {
            return;
        }

        if (!this.isCurrentlyPlaying) {
            throw new PlayerNotRunning();
        }

        System.out.println("setting new path");

        List<Direction> steps;

        this.runnablePlayer.pause();

        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (steps.size() == 0) {
            System.out.println("steps length are 0");
        }

        lastDirection.ifPresent(steps::add);

        if (isNewPathBetter(steps)) {
            changePlayerPath(steps);
        } else {
            stayWithSamePathAfterTeleported(steps);
        }

        if(this.runnablePlayer.isThereStepsToRunUntilFinished()) {
            this.runnablePlayer.listenToStepsToRunFinished().subscribe((integer, throwable) -> {
                if (throwable != null) {
                    logger.error("listenToStepsToRunFinished error", throwable);
                    throwable.printStackTrace();
                    return;
                }

                this.runnablePlayer.resumeAndResetStepsToRun();
            });
        } else {
            this.runnablePlayer.resumeAndResetStepsToRun();
        }
    }

    private void stayWithSamePathAfterTeleported(List<Direction> steps) {
        logger.debug("[ComputerPlayer]", "[" + this.getName() + "]", "[On Teleport]", "continue with the same path");

        try {
            this.continueWithOldPathAfterTeleportation();
        } catch (PlayerNotRunning playerNotRunning) {
            logger.error("Player not running exception", playerNotRunning);
            logger.debug("player on the first step teleported - " + this.runnablePlayer.getCurrentStep() + " finding new path");
            changePlayerPath(steps);
        }
    }


    private boolean isNewPathBetter(List<Direction> steps) {
        return steps.size() < this.runnablePlayer.getTotalStepsLeft();
    }

    private void continueWithOldPathAfterTeleportation() throws PlayerNotRunning {
        ObjectAssertion.requireNonNull(currentCell, "Current Cell can't be null");

        // TODO - HANDLE IF IN MIDDLE OF A THIS FUNCTION THE CANDY DISAPPEAR

        // Direction set to right with no true reason it will only affect if it will need to handle nested teleportation
        Direction directionToMove = Direction.RIGHT;

        if (isThereNearWall()) {
            // In prefect maze this will always happen
            logger.debug("[Computer Player]", "[" + this.getName() + "]", "[Continue with old path]", "Move to wall");
            moveToWall();
            return;
        } else if (hasNearCellWithoutTeleportCandy()) {
            logger.debug("[Computer Player]", "[" + this.getName() + "]", "[Continue with old path]", "Move to other cell without teleport candy");
            moveAndReturn(getDirectionOfNearCellWithoutTeleportCandy());
            return;
        } else {
            // TODO - HANDLE NESTED TELEPORTATION
            logger.debug("[Computer Player]", "[" + this.getName() + "]", "Continuing with the same path", "No Walls or cells without teleportation candies");
        }

        Direction oppositeStep = Direction.getOppositeDirection(directionToMove);
        this.runnablePlayer.injectStep(oppositeStep);
//        this.runnablePlayer.move(oppositeStep);
    }

    private void moveToWall() {
        assert this.currentCell != null;

        this.returnAfterTeleport.compareAndSet(false, true);

        this.runnablePlayer.injectStep(getNearWall());

        setReturnAfterTeleportWhenStepsToRunFinished();

        // 2 because the it already stopped for other move
        this.runnablePlayer.resumeForNSteps(2);

    }

    private void setReturnAfterTeleportWhenStepsToRunFinished() {
        this.runnablePlayer.listenToStepsToRunFinished().subscribe((integer, throwable) -> {
            if (throwable != null) {
                logger.error("listenToStepsToRunFinished error", throwable);
                throwable.printStackTrace();
                return;
            }

            this.returnAfterTeleport.compareAndSet(true, false);
        });
    }

    private void moveAndReturn(Direction direction) {
        assert direction != null;

        this.returnAfterTeleport.compareAndSet(false, true);

        this.runnablePlayer.injectStep(direction);
        this.runnablePlayer.injectStep(Direction.getOppositeDirection(direction));

        setReturnAfterTeleportWhenStepsToRunFinished();

        this.runnablePlayer.resumeForNSteps(3);
    }

    private boolean hasNearCellWithoutTeleportCandy() {
        return this.currentCell.getNeighbors()
                .entrySet()
                .stream()
                .anyMatch(this::isNeighborDoesntHaveTeleportCandy);
    }

    private Direction getDirectionOfNearCellWithoutTeleportCandy() {
        return this.currentCell.getNeighbors()
                .entrySet()
                .stream()
                .filter(this::isNeighborDoesntHaveTeleportCandy)
                .map(this::getScoreForNeighborCellMovement)
                .sorted()
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> Direction.RIGHT);
    }

    private Map.Entry<Direction, Integer> getScoreForNeighborCellMovement(Map.Entry<Direction, Cell.NeighborCell> directionNeighborCellEntry) {
        Direction direction = directionNeighborCellEntry.getKey();
        ArrayList<Candy> candies = directionNeighborCellEntry.getValue().getCell().getCandies();

        int score = candies == null ? 0 : candies.stream().mapToInt(Candy::getCandyStrength).sum();
        return new AbstractMap.SimpleEntry<>(direction, score);
    }

    private boolean isNeighborDoesntHaveTeleportCandy(Map.Entry<Direction, Cell.NeighborCell> directionNeighborCellEntry) {
        if (directionNeighborCellEntry == null) {
            return false;
        }

        Cell.NeighborCell neighborCellEntryValue = directionNeighborCellEntry.getValue();
        if (neighborCellEntryValue == null) {
            return false;
        }

        Cell cell = neighborCellEntryValue.getCell();

        return cell != null && cell.getCandies().stream().allMatch(candy -> candy != null && candy.getType() != CandyPowerType.Location);
    }

    private Direction getNearWall() {
        return this.currentCell.getNeighbors()
                .entrySet()
                .stream()
                .filter(directionNeighborCellEntry -> directionNeighborCellEntry.getValue() == null)
                .findFirst()
                .get()
                .getKey();
    }

    private boolean isThereNearWall() {
        ObjectAssertion.requireNonNull(currentCell, "Current Cell can't be null");
        return this.currentCell.getNeighbors().containsValue(null);
    }

    private void changePlayerPath(List<Direction> steps) {

        this.playerThread.setName(this.playerThread.getName() + " - prev path");
        this.runnablePlayer.stop();
        RunnableComputerPlayer prevRunnable = this.runnablePlayer;
        this.runnablePlayer = null;

        try {
            this.runnablePlayer = new RunnableComputerPlayer(prevRunnable, steps);
        } catch (PlayerAlreadyHaveRunningThreadException e) {
            handlePlayerAlreadyHaveRunningThreadException(e);
            return;
        }

        this.playerThread = createPlayerThread();

        this.playerThread.start();
    }

    private void handlePlayerAlreadyHaveRunningThreadException(PlayerAlreadyHaveRunningThreadException e) {
        // This shouldn't happen because we setting the runnable player to null
        logger.error("[Computer Player][Already Have Running Thread]");
        e.printStackTrace();
    }

    private Coordinate findClosestExit(Maze maze) {
        // TODO - Find the closest exit from the current location
        return null;
    }

    @Override
    public void onPlayerFinished() {
        super.onPlayerFinished();
        logger.info("[Computer Player][onPlayerFinished]");

        if (this.runnablePlayer != null) {
            logger.info("[Computer Player][Finish][Stopping Thread]");

            synchronized (runnablePlayer) {
                runnablePlayer.stop();
            }

            this.runnablePlayer = null;
        }

        if (this.playerThread != null) {
            this.playerThread = null;
        }

        isCurrentlyPlaying = false;
    }

    RunnableComputerPlayer getRunnablePlayer() {
        return this.runnablePlayer;
    }

    public int getDelayMovementInMs() {
        return delayMovementInMs;
    }

    @Override
    public BasePlayer clone() {
        return new ComputerPlayer(Utils.clone(getLocation()), delayMovementInMs);
    }
}
