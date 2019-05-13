package player.ComputerPlayer;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Candy.Candy;
import Maze.Candy.CandyPowerType;
import Maze.Cell;
import Maze.Maze;
import player.BasePlayer;
import player.exceptions.PlayerNotRunning;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class ComputerPlayer extends BasePlayer {

    private Maze maze;
    private Cell currentCell;
    private Coordinate endingLocation;

    private Thread playerThread = null;
    private RunnableComputerPlayer runnablePlayer = null;
    private volatile boolean isCurrentlyPlaying = false;
    private boolean returnAfterTeleport = false;

    private int defaultDelayMovementInMs = 100;
    private Stack<Direction> returnToPathStack = new Stack<>();

    /**
     * Human Player Constructor
     *
     * @param location Starting location of the player
     */
    public ComputerPlayer(Coordinate location) {
        super(location);

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
        return createRunningThread(maze, endingLocation, defaultDelayMovementInMs);
    }

    /**
     * Start The player
     *
     * @param maze           Current Maze
     * @param endingLocation The location that this player is heading
     * @param stepSpeedMs    How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, Coordinate endingLocation, int stepSpeedMs) {
        // TODO - YOU CAN USE LISTENER FOR CANDY COLLECTED OR CANDY DISAPPEARED AND FILTER ONLY TO LOCATION CANDIES
        // TODO - IF CANDY DISAPPEARED THEN RECALCULATE

        this.maze = maze;
        this.endingLocation = endingLocation;

        this.currentCell = this.maze.getCell(this.getLocation());

        Direction[] steps;
        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (steps.length == 0) {
            System.out.println("steps length are 0");
        }

        this.isCurrentlyPlaying = true;

        if (this.runnablePlayer != null) {
            this.runnablePlayer.stop();
        }
        this.runnablePlayer = new RunnableComputerPlayer(this, steps, stepSpeedMs);
        this.playerThread = createPlayerThread();
        return this.playerThread;
    }

    private Thread createPlayerThread() {
        Thread playerThread = new Thread(runnablePlayer);
        playerThread.setName("Computer " + this.getName() + " Thread");
        return playerThread;
    }

    /**
     * Start The player and the player choose the closest exit
     *
     * @param maze        Current Maze
     * @param stepSpeedMs How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, int stepSpeedMs) {
        return this.createRunningThread(maze, this.findClosestExit(maze), stepSpeedMs);
    }

    @Override
    public void onPlayerTeleported() throws PlayerNotRunning {
        if (returnAfterTeleport) {
            return;
        }

        if(!this.returnToPathStack.empty()) {
            this.runnablePlayer.move(this.returnToPathStack.pop());
            return;
        }

        if (!this.isCurrentlyPlaying) {
            throw new PlayerNotRunning();
        }

        System.out.println("setting new path");

        Direction[] steps;

        this.runnablePlayer.pause();

        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (steps.length == 0) {
            System.out.println("steps length are 0");
        }

        if (!isNewPathBetter(steps)) {
            System.out.println("continue with the same path");

            try {
                this.continueWithOldPathAfterTeleportation();
            } catch (PlayerNotRunning playerNotRunning) {
                System.out.println("player on the first step teleported - " + this.runnablePlayer.getCurrentStep() + " finding new path");
                changePlayerPath(steps);
            }
        } else {
            changePlayerPath(steps);

        }

        this.runnablePlayer.resume();

    }


    private boolean isNewPathBetter(Direction[] steps) {
        return steps.length < this.runnablePlayer.getTotalStepsLeft();
    }

    private void continueWithOldPathAfterTeleportation() throws PlayerNotRunning {
        if (this.currentCell == null) {
            throw new IllegalStateException("Current Cell can't be null");
        }

        // TODO - HANDLE IF IN MIDDLE OF A THIS FUNCTION THE CANDY DISAPPEAR

        Direction directionToMove = Direction.RIGHT;

        if (isThereNearWall()) {
            moveToWall();
            return;
        } else if(hasNearCellWithoutTeleportCandy()) {
            moveAndReturn(getDirectionOfNearCellWithoutTeleportCandy());
            return;
        } else {
            // TODO - HANDLE NESTED TELEPORTATION
            System.out.println("No Walls or cells without teleportation candies ");
        }

        Direction oppositeStep = Direction.getOppositeDirection(directionToMove);
        this.returnToPathStack.push(oppositeStep);
        this.runnablePlayer.move(directionToMove);
//        this.runnablePlayer.move(oppositeStep);

        // TODO - Check if need to move to the same direction before the teleportation
    }

    private void moveToWall() {
        assert this.currentCell != null;

        this.returnAfterTeleport = true;
        synchronized (this.runnablePlayer) {
            this.runnablePlayer.resume();
            this.runnablePlayer.move(getNearWall());
            this.runnablePlayer.pause();
        }
        this.returnAfterTeleport = false;
    }

    private void moveAndReturn(Direction direction) {
        assert direction != null;

        this.returnAfterTeleport = true;

        this.runnablePlayer.resume();
        this.runnablePlayer.move(direction);
        this.runnablePlayer.pause();

        Direction oppositeStep = Direction.getOppositeDirection(direction);

        this.runnablePlayer.resume();
        this.runnablePlayer.move(oppositeStep);
        this.runnablePlayer.pause();

        this.returnAfterTeleport = false;

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
        if(directionNeighborCellEntry == null) {
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
        assert this.currentCell != null;
        return this.currentCell.getNeighbors().containsValue(null);
    }

    private void changePlayerPath(Direction[] steps) {
        this.returnToPathStack.removeAllElements();

        this.runnablePlayer.stop();
        this.runnablePlayer = new RunnableComputerPlayer(this.runnablePlayer, steps);

        this.playerThread = createPlayerThread();

        this.playerThread.start();
    }

    private Coordinate findClosestExit(Maze maze) {
        // TODO - Find the closest exit from the current location
        return null;
    }
}
