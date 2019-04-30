package player;

import Helpers.Direction;

public class RunnableComputerPlayer implements Runnable {

    private final ComputerPlayer player;
    private Direction[] directions;
    private int stepSpeedMs;

    private boolean run = true;

    public RunnableComputerPlayer(ComputerPlayer player, Direction[] directions, int stepSpeedMs) {
        this.player = player;
        this.directions = directions;
        this.stepSpeedMs = stepSpeedMs;
    }

    public RunnableComputerPlayer(RunnableComputerPlayer computerPlayer, Direction[] directions) {
        this.player = computerPlayer.player;
        this.directions = directions;
        this.stepSpeedMs = computerPlayer.stepSpeedMs;
    }

    @Override
    public void run() {

        Direction[] directions1 = this.directions;
        for (int i = 0; i < directions1.length && run; i++) {
            Direction direction = directions1[i];

            System.out.println("Computer moved " + direction);
            synchronized (player) {
                player.move(direction);
            }
            try {
                Thread.sleep(stepSpeedMs);
            } catch (InterruptedException e) {
                System.out.println("Error in thread sleep in computer player move");
                e.printStackTrace();
            }
        }
    }

    public void stopRunning() {
        this.run = false;
    }
}
