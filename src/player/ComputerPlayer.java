package player;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;
import Maze.Maze;

import java.util.Arrays;
import java.util.stream.Stream;

public class ComputerPlayer extends BasePlayer {

    /**
     * Human Player Constructor
     *
     * @param location Starting location of the player
     */
    public ComputerPlayer(Coordinate location) {
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
    public Thread createRunningThread(Maze maze, Coordinate endingLocation, int stepSpeedMs) {
        // TODO - IF CANDY DISAPPEARED THEN RECALCULATE
        // TODO - YOU CAN USE LISTENER FOR CANDY COLLECTED OR CANDY DISAPPEARED AND FILTER ONLY TO LOCATION CANDIES

        Direction[] steps;
        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Stream<Direction> stepsStream = Arrays.stream(steps);

        return new Thread(() ->
                              stepsStream.forEach(direction -> {
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
        Direction[] steps;

        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), null, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Stream<Direction> stepsStream = Arrays.stream(steps);

        return new Thread(() ->
                              stepsStream.forEach(direction -> {
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
