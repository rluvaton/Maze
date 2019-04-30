package player;

import Helpers.Direction;
import player.exceptions.PlayerNotRunning;

public class RunnableComputerPlayer implements Runnable {

    private final ComputerPlayer player;
    private Direction[] directions;
    private int stepSpeedMs;

    private int currentStep = -1;
    private Direction lastStep;

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private Object pauseLock = new Object();

    public RunnableComputerPlayer(ComputerPlayer player, Direction[] directions, int stepSpeedMs) {
        this.player = player;
        this.directions = directions;
        this.stepSpeedMs = stepSpeedMs;
    }

    public RunnableComputerPlayer(RunnableComputerPlayer computerPlayer, Direction[] directions) {
        this.player = computerPlayer.player;
        this.directions = directions;
        this.stepSpeedMs = computerPlayer.stepSpeedMs;
        this.running = computerPlayer.running;
        this.paused = computerPlayer.paused;
        this.pauseLock = computerPlayer.pauseLock;
    }

    @Override
    public void run() {

        Direction[] directions1 = this.directions;
        for (currentStep = 0; currentStep < directions1.length; currentStep++) {
            if(!this.running) {
                break;
            }

            if (paused) {
                try {
                    pauseLock.wait(); // will cause this Thread to block until
                    // another thread calls pauseLock.notifyAll()
                    // Note that calling wait() will
                    // relinquish the synchronized lock that this
                    // thread holds on pauseLock so another thread
                    // can acquire the lock to call notifyAll()
                    // (link with explanation below this code)
                } catch (InterruptedException ex) {
                    break;
                }

                if (!running) { // running might have changed since we paused
                    break;
                }
            }

            Direction direction = directions1[currentStep];

            this.move(direction);
        }
    }

    public void move(Direction direction) {
        System.out.println("Computer moved " + direction);
        synchronized (player) {
            player.move(direction);

            this.lastStep = direction;
        }

        try {
            Thread.sleep(stepSpeedMs);
        } catch (InterruptedException e) {
            System.out.println("Error in thread sleep in computer player move");
            e.printStackTrace();
        }
    }

    public void undoStep() throws PlayerNotRunning {
        this.move(Direction.getOppositeDirection(this.getLastStep()));
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getTotalStepsLeft() {
        return this.directions.length - this.currentStep;
    }

    public Direction getLastStep() throws PlayerNotRunning {
        if (this.lastStep == null) {
            throw new PlayerNotRunning();
        }

        return lastStep;
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void restart() {
        running = true;
        resume();
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
}
