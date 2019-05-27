package player.ComputerPlayer;

import Helpers.ControlledRunnable.ControlledRunnable;
import Helpers.ControlledRunnable.RunnableStoppedRunningException;
import Helpers.DebuggerHelper;
import Helpers.Direction;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.exceptions.InvalidDirectionException;
import player.exceptions.PlayerAlreadyHaveRunningThreadException;
import player.exceptions.PlayerNotRunning;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static Logger.LoggerManager.logger;

public class RunnableComputerPlayer extends ControlledRunnable {

    private final ComputerPlayer player;
    private List<Direction> directions;
    private int stepSpeedMs;

    private int currentStep = -1;
    private Direction lastStep;

    private Subject onDestroySub = PublishSubject.create();

    private Queue<Direction> injectedSteps = new LinkedList<>();

    private Subject<Integer> stepsToRunFinished = PublishSubject.create();

    /**
     * -1 as the value meaning that it shouldn't stop
     */
    private volatile AtomicInteger stepsToRunBeforePause = new AtomicInteger(-1);

    public RunnableComputerPlayer(RunnableComputerPlayer computerPlayer, Direction[] directions) throws PlayerAlreadyHaveRunningThreadException {
        this(computerPlayer, Arrays.asList(directions));
    }

    public RunnableComputerPlayer(RunnableComputerPlayer computerPlayer, List<Direction> directions) throws PlayerAlreadyHaveRunningThreadException {
        this(computerPlayer.player, directions, computerPlayer.stepSpeedMs);
    }

    public RunnableComputerPlayer(ComputerPlayer player, Direction[] directions, int stepSpeedMs) throws PlayerAlreadyHaveRunningThreadException {
        this(player, Arrays.asList(directions), stepSpeedMs);
    }

    public RunnableComputerPlayer(ComputerPlayer player, List<Direction> directions, int stepSpeedMs) throws PlayerAlreadyHaveRunningThreadException {
        this.player = player;
        this.directions = directions;
        this.stepSpeedMs = stepSpeedMs;

        if (player.getRunnablePlayer() == this) {
            throw new PlayerAlreadyHaveRunningThreadException();
        }
    }

    @Override
    public void run() {

        if (DebuggerHelper.isInDebugMode()) {
            waitToKey();
        }

        int size = this.directions.size();
        for (currentStep = 0; currentStep < size; currentStep++) {
            if (moveTheInjectedStepsIfHaveSome()) {
                break;
            }

            if (moveInDirection(this.directions.get(currentStep))) {
                break;
            }
        }

        destroy();
    }

    private boolean moveTheInjectedStepsIfHaveSome() {
        while (!this.injectedSteps.isEmpty()) {
            if (moveInDirection(this.injectedSteps.poll())) {
                return true;
            }
        }

        return false;
    }

    private boolean moveInDirection(Direction direction) {
        if(stepsToRunBeforePause.getAcquire() == 0) {
            this.pause();
            this.stepsToRunFinished.onNext(0);
        }

        try {
            super.threadActionManagement();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        } catch (RunnableStoppedRunningException e) {
            return true;
        }

        if (DebuggerHelper.isInDebugMode()) {
            pause();
        }

        this.immediateMove(direction);

        if (!DebuggerHelper.isInDebugMode()) {
            delay();
        }

        return false;
    }

    public synchronized void move(Direction direction) {
        if (moveInDirection(direction)) {
            // should stop moving and exit the loop
        }
    }

    private void immediateMove(Direction direction) {
        logger.info("[Computer Player][Move]: Direction " + direction);
        synchronized (player) {
            try {
                player.move(direction);
            } catch (InvalidDirectionException invalidDirectionEx) {
                handleInvalidDirectionException(direction);
            }

            this.lastStep = direction;
        }

        updateStepsToRunBeforePause();
    }

    private void updateStepsToRunBeforePause() {
        if (stepsToRunBeforePause.getAcquire() > -1) {
            stepsToRunBeforePause.decrementAndGet();
        }
    }

    private void handleInvalidDirectionException(Direction direction) {
        logger.warn("[Error][Computer Player][Invalid Move]: Direction " + direction);
    }

    private void waitToKey() {
        Thread thread = new Thread(new Runnable() {
            private volatile boolean isKeyListenerThreadRunning = true;

            @Override
            public void run() {

                onDestroySub.subscribe(none -> isKeyListenerThreadRunning = false);

                DebuggerHelper.getInstance().getSingleKeyPressedObs(KeyEvent.VK_SPACE)
                        .takeUntil(onDestroySub)
                        .subscribe(new Consumer<KeyEvent>() {
                            @Override
                            public synchronized void accept(KeyEvent keyEvent) {
                                System.out.println("On " + KeyEvent.getKeyText(keyEvent.getKeyCode()) + " key");
                                RunnableComputerPlayer.this.resume();
                            }
                        });

                while (isKeyListenerThreadRunning) {
                }

                logger.info("[Computer Player][Debug] Finishing thread " + Thread.currentThread().getName());

            }
        });
        thread.setName("Wait for key Thread");
        thread.start();
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

    public void undoStep() throws PlayerNotRunning {
        this.move(Direction.getOppositeDirection(this.getLastStep()));
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getTotalStepsLeft() {
        return this.directions.size() - this.currentStep;
    }

    public Direction getLastStep() throws PlayerNotRunning {
        if (this.lastStep == null) {
            throw new PlayerNotRunning();
        }

        return lastStep;
    }

    public void resumeUtilQueueFinished() {
        this.stepsToRunBeforePause.set(this.injectedSteps.size());
        this.resume();
    }

    public void resumeForNSteps(int n) {
        if(n < -1) {
            n = -1;
        }

        this.stepsToRunBeforePause.set(n);
        this.resume();
    }

    public void resetStepsToRunBeforePause() {
        this.stepsToRunBeforePause.set(-1);
    }

    public void resumeAndResetStepsToRun() {
        this.resetStepsToRunBeforePause();
        this.resume();
    }

    public void injectStep(Direction injectedStep) {
        this.injectedSteps.add(injectedStep);
    }

    public boolean isThereStepsToRunUntilFinished() {
        return this.stepsToRunBeforePause.get() != -1;
    }

    public void removeInjectedSteps() {
        this.injectedSteps.clear();
    }

    public Single<Integer> listenToStepsToRunFinished() {
        return this.stepsToRunFinished.firstOrError();
    }

    @Override
    public void stop() {
        removeInjectedSteps();
        this.resetStepsToRunBeforePause();
        super.stop();
    }

    private void destroy() {
        this.onDestroySub.onNext(true);
    }
}
