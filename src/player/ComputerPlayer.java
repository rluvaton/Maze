package player;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Maze;

public class ComputerPlayer extends BasePlayer {

    private Maze maze;
    private Coordinate endingLocation;
    private Thread playerThread = null;
    private RunnableComputerPlayer runnablePlayer = null;
    private volatile boolean isCurrentlyPlaying = false;

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

        this.maze = maze;
        this.endingLocation = endingLocation;

        Direction[] steps;
        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        this.isCurrentlyPlaying = true;

        if(this.runnablePlayer != null) {
            this.runnablePlayer.stopRunning();
        }
        this.runnablePlayer = new RunnableComputerPlayer(this, steps, stepSpeedMs);
        this.playerThread = new Thread(runnablePlayer);
        return this.playerThread;
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
    public void onPlayerTeleported() {
        if (!this.isCurrentlyPlaying) {
            return;
        }

//        this.isCurrentlyPlaying = false;
        System.out.println("setting new path");

        Direction[] steps;

        if(this.runnablePlayer != null) {
            this.runnablePlayer.stopRunning();
        }

        try {
            steps = maze.getSolverAdapter().solveMaze(maze, this.getLocation(), endingLocation, true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(this.runnablePlayer != null) {
            this.runnablePlayer = new RunnableComputerPlayer(this.runnablePlayer, steps);
        } else {
            this.runnablePlayer = new RunnableComputerPlayer(this, steps, 1000);
        }

        this.playerThread = new Thread(runnablePlayer);
        this.playerThread.start();
    }

    private Coordinate findClosestExit(Maze maze) {
        // TODO - Find the closest exit from the current location
        return null;
    }
}
