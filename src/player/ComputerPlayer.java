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

    /**
     * Start The player
     *
     * @param maze           Current Maze
     * @param endingLocation The location that this player is heading
     * @param stepSpeedMs    How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, Tuple<Integer, Integer> endingLocation, int stepSpeedMs) {
        Stack<Direction> steps;
        try {
            steps = DFSSolver.getSolvePathSteps(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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
     * @param maze        Current Maze
     * @param stepSpeedMs How much milliseconds to wait between each move
     * @return Returns thread of the computer steps with sleep at the stepSpeedMs variable
     */
    public Thread createRunningThread(Maze maze, int stepSpeedMs) {
        // TODO - Find the closest exit from the current location
        Stack<Direction> steps;

        try {
            steps = DFSSolver.getSolvePathSteps(maze, this.getLocation(), null, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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
