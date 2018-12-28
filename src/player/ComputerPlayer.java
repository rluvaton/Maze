package player;

import Helpers.Direction;
import Helpers.Tuple;
import Maze.MazeSolver.DFS.DFSCell;

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

    public void start(DFSCell[][] maze, Tuple<Integer, Integer> endingLocation, int stepSpeed) {
        Stack<Tuple<Integer, Integer>> steps = new Stack<>();
        steps.forEach(nextLocation -> {

        });
    }
}
