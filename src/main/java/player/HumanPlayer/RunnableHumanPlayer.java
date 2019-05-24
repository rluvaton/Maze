package player.HumanPlayer;

import Helpers.ControlledRunnable.ControlledRunnable;
import Helpers.ControlledRunnable.RunnableStoppedRunningException;
import Helpers.Direction;
import player.exceptions.InvalidDirectionException;

import static Logger.LoggerManager.logger;

public class RunnableHumanPlayer extends ControlledRunnable {

    private HumanPlayer player;
    private Direction direction;
    private long stepSpeedMs;

    private static int DEFAULT_STEP_SPEED_MS = 200;

    public RunnableHumanPlayer(HumanPlayer player) {
        this(player, player.getStepSpeed());
    }

    public RunnableHumanPlayer(HumanPlayer player, long stepSpeedMs) {
        this.player = player;
        this.stepSpeedMs = stepSpeedMs;
    }

    @Override
    public void run() {
        logger.debug("[Player][Start Running]");

        while (!this.moveInDirection()) {

        }

        logger.debug("[Player][Finish Running]");
    }

    private boolean moveInDirection() {
        if (direction == null) {
            super.pause();
        }

        try {
            super.threadActionManagement();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        } catch (RunnableStoppedRunningException e) {
            return true;
        }

        this.immediateMove();
        delay();

        return false;
    }

    private void immediateMove() {
        synchronized (player) {
            try {
                player.move(this.direction);
            } catch (InvalidDirectionException invalidDirectionEx) {
                handleInvalidDirectionException(this.direction);
            }
        }
    }

    private void handleInvalidDirectionException(Direction direction) {
        logger.warn("Invalid Human move " + direction);
    }

    private void delay() {
        try {
            Thread.sleep(stepSpeedMs);
        } catch (InterruptedException e) {
            handleDelayException(e);
        }
    }

    private void handleDelayException(InterruptedException e) {
        logger.warn("Error in thread sleep in human player move");
        e.printStackTrace();
    }

    public void stop() {
        this.updateDirection(null);
        super.stop();
    }

    public void pause() {
        this.updateDirection(null);
        super.pause();
    }

    public void setDirection(Direction direction) {

        logger.info("[Player][Updating direction] - " + direction);
        this.updateDirection(direction);

        if(direction == null) {
            super.pause();
            return;
        }

        super.resume();

    }

    public void setStepSpeedMs(long stepSpeedMs) {
        this.stepSpeedMs = stepSpeedMs;
    }

    private synchronized void updateDirection(Direction direction) {
        this.direction = direction;
    }
}
