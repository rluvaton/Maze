package GUI.MazeGame;

import Helpers.ControlledRunnable.ControlledRunnable;
import Helpers.ControlledRunnable.RunnableStoppedRunningException;

public class ControlledTimer {
    private Thread timer;
    private ControlledRunnableTimer controlledTimer;

    public ControlledTimer(int delay, Runnable onFinish) {
        controlledTimer = new ControlledRunnableTimer(delay, onFinish);
        timer = new Thread(controlledTimer);
    }

    public void start() {
        timer.start();
    }

    public void pause() {
        controlledTimer.pause();
    }

    public void resume() {
        controlledTimer.firstTimeInMs = System.currentTimeMillis() - controlledTimer.msPassed;
        controlledTimer.resume();
    }

    public void reschedule(int totalTime) {
        controlledTimer.pause();
        controlledTimer.totalTimeInMs = totalTime;
        controlledTimer.msPassed = 0;
        controlledTimer.firstTimeInMs = System.currentTimeMillis();
        controlledTimer.resume();
    }

    public void cancel() {
        controlledTimer.stop();
        controlledTimer = null;
    }

    private static class ControlledRunnableTimer extends ControlledRunnable {

        private long firstTimeInMs;
        private long totalTimeInMs = 0;
        private long msPassed = 0;
        private Runnable onFinish;

        public ControlledRunnableTimer(long totalTimeInMs, Runnable onFinish) {
            this.totalTimeInMs = totalTimeInMs;
            this.onFinish = onFinish;
        }

        @Override
        public void run() {
            firstTimeInMs = System.currentTimeMillis();
            while (totalTimeInMs > msPassed) {
                try {
                    super.threadActionManagement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                } catch (RunnableStoppedRunningException e) {
                    return;
                }
                try {
                    Thread.sleep(1);

                    msPassed = System.currentTimeMillis() - firstTimeInMs;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            onFinish.run();
        }
    }
}
