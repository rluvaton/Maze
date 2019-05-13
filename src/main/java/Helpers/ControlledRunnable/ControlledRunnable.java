package Helpers.ControlledRunnable;

public abstract class ControlledRunnable implements Runnable {

    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    protected final void threadActionManagement() throws InterruptedException, RunnableStoppedRunningException {
        if (!this.running) {
            throw new RunnableStoppedRunningException();
        }

        if (paused) {
            synchronized (pauseLock) {
                pauseLock.wait();
                /*
                 will cause this Thread to block until
                 another thread calls pauseLock.notifyAll()
                 Note that calling wait() will
                 relinquish the synchronized lock that this
                 thread holds on pauseLock so another thread
                 can acquire the lock to call notifyAll()
                */
            }

            // running might have changed since we paused
            if (!running) {
                throw new RunnableStoppedRunningException();
            }
        }
    }

    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
    }

    public final void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }

}
