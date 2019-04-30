package player;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Maze;
import player.exceptions.PlayerNotRunning;

public class ComputerPlayer extends BasePlayer {

    private Maze maze;
    private Coordinate endingLocation;
    private Thread playerThread = null;
    private RunnableComputerPlayer runnablePlayer = null;
    private volatile boolean isCurrentlyPlaying = false;
    private boolean returnAfterTeleport = false;

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

        if(steps.length == 0) {
            System.out.println("steps length are 0");
        }

        this.isCurrentlyPlaying = true;

        if (this.runnablePlayer != null) {
            this.runnablePlayer.stop();
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
    public void onPlayerTeleported() throws PlayerNotRunning {
        if(returnAfterTeleport) {
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

        if(steps.length == 0) {
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
//        if(isThereNearWall()) {
//            moveToWall();
//        } else {
//            moveRight();
//            moveLeft();
//        }
        Direction oppositeStep = Direction.getOppositeDirection(this.runnablePlayer.getLastStep());
        this.returnAfterTeleport = true;
        this.runnablePlayer.move(oppositeStep);
        this.returnAfterTeleport = false;
    }

    private void changePlayerPath(Direction[] steps) {
        this.runnablePlayer.stop();
        this.runnablePlayer = new RunnableComputerPlayer(this.runnablePlayer, steps);

        this.playerThread = new Thread(runnablePlayer);

        this.runnablePlayer.restart();

        this.playerThread.start();
    }

    private Coordinate findClosestExit(Maze maze) {
        // TODO - Find the closest exit from the current location
        return null;
    }
}
