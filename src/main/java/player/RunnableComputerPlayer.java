package player;

import Helpers.ControlledRunnable;
import Helpers.DebuggerHelper;
import Helpers.Direction;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.exceptions.InvalidDirectionException;
import player.exceptions.PlayerNotRunning;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class RunnableComputerPlayer extends ControlledRunnable {

    private final ComputerPlayer player;
    private List<Direction> directions;
    private int stepSpeedMs;

    private int currentStep = -1;
    private Direction lastStep;

    private Subject onDestroySub = PublishSubject.create();

    public RunnableComputerPlayer(ComputerPlayer player, Direction[] directions, int stepSpeedMs) {
        this(player, Arrays.asList(directions), stepSpeedMs);
    }

    public RunnableComputerPlayer(ComputerPlayer player, List<Direction> directions, int stepSpeedMs) {
        this.player = player;
        this.directions = directions;
        this.stepSpeedMs = stepSpeedMs;
    }

    public RunnableComputerPlayer(RunnableComputerPlayer computerPlayer, Direction[] directions) {
        this.player = computerPlayer.player;
        this.directions = Arrays.asList(directions);
        this.stepSpeedMs = computerPlayer.stepSpeedMs;
    }

    @Override
    public void run() {

        if(DebuggerHelper.isInDebugMode()) {
            waitToKey();
        }

        // recomputing every loop cycle in purpose (the size can change)
        for (currentStep = 0; currentStep < this.directions.size(); currentStep++) {
            if (moveInDirection(this.directions.get(currentStep))) {
                break;
            }
        }

        destroy();
    }

    private boolean moveInDirection(Direction direction) {
        try {
            if (super.threadActionManagement()) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }

        this.immediateMove(direction);
        return false;
    }

    public synchronized void move(Direction direction) {
        if(moveInDirection(direction)) {
            // should stop moving and exit the loop
        }
//        this.directions.add(currentStep, direction);
    }

    private void immediateMove(Direction direction) {

        if(DebuggerHelper.isInDebugMode()) {
            pause();
        }

        System.out.println("Computer moved " + direction);
        synchronized (player) {
            try {
                player.move(direction);
            } catch (InvalidDirectionException invalidDirectionEx) {
                System.out.println("Invalid computer move " + direction);
            }

            this.lastStep = direction;
        }


        if(!DebuggerHelper.isInDebugMode()) {
            delay();
        }
    }

    private void waitToKey() {
        Thread thread = new Thread(new Runnable() {
            private volatile boolean isKeyListenerThreadRunning = true;

            @Override
            public void run() {

                onDestroySub.subscribe(none -> {
                    isKeyListenerThreadRunning = false;
                });

                DebuggerHelper.getInstance().getSingleKeyPressedObs(KeyEvent.VK_SPACE)
                        .takeUntil(onDestroySub)
                        .subscribe(new Consumer<KeyEvent>() {
                            @Override
                            public synchronized void accept(KeyEvent keyEvent) throws Exception {
                                System.out.println("On " + KeyEvent.getKeyText(keyEvent.getKeyCode()) + " key");
                                RunnableComputerPlayer.this.resume();
                            }
                        });

                while (isKeyListenerThreadRunning);

                System.out.println("Finishing thread " + Thread.currentThread().getName());

            }
        });
        thread.setName("Wait for key Thread");
        thread.start();
    }

    private void delay() {
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
        return this.directions.size() - this.currentStep;
    }

    public Direction getLastStep() throws PlayerNotRunning {
        if (this.lastStep == null) {
            throw new PlayerNotRunning();
        }

        return lastStep;
    }

    private void destroy() {
        this.onDestroySub.onNext(true);
    }
}
