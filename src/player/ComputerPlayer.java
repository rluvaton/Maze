package player;

import Helpers.Direction;
import Helpers.Tuple;
import Maze.Maze;
import Maze.MazeSolver.DFS.DFSCell;
import Maze.MazeSolver.DFS.DFSSolver;

import java.util.Stack;

public class ComputerPlayer extends BasePlayer {

    /**
     * Human Player Constructor
     *
     * @param location Starting location of the player
     */
    public ComputerPlayer(Tuple<Integer, Integer> location) {
        super(location);
    }

    // region Base Player abstract functions

    /**
     * Move Top
     */
    @Override
    public void top() {
        this.notifyMoved(Direction.TOP);
    }

    /**
     * Move Right
     */
    @Override
    public void right() {
        this.notifyMoved(Direction.RIGHT);
    }

    /**
     * Move Bottom
     */
    @Override
    public void bottom() {
        this.notifyMoved(Direction.BOTTOM);
    }

    /**
     * Move Left
     */
    @Override
    public void left() {
        this.notifyMoved(Direction.LEFT);
    }

    // endregion

    /**
     * Start The player
     *
     * @param maze           Current Maze
     * @param endingLocation The location that this player is heading
     * @param stepSpeedMs    How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread start(Maze maze, Tuple<Integer, Integer> endingLocation, int stepSpeedMs) {
        Stack<Direction> steps = DFSSolver.getSolvePathSteps(maze, this.getLocation(), endingLocation, true);

        return new Thread(() ->
                steps.forEach(direction -> {
                    System.out.println("Computer moved " + direction);
                    super.move(direction);
                    try {
                        Thread.sleep(stepSpeedMs);
                    } catch (InterruptedException e) {
                        System.out.println("Error in thread sleep in computer player move");
                        e.printStackTrace();
                    }
                }));

    }

    /**
     * Start The player and the player choose the closest exit
     *
     * @param maze           Current Maze
     * @param stepSpeedMs    How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread start(Maze maze, int stepSpeedMs) {
        // TODO - Find the closest exit from the current location
        Stack<Direction> steps = DFSSolver.getSolvePathSteps(maze, this.getLocation(), null, true);

        return new Thread(() ->
                steps.forEach(direction -> {
                    System.out.println("Computer moved " + direction);
                    super.move(direction);
                    try {
                        Thread.sleep(stepSpeedMs);
                    } catch (InterruptedException e) {
                        System.out.println("Error in thread sleep in computer player move");
                        e.printStackTrace();
                    }
                }));

    }
}
