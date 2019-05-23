package player;

import Helpers.Direction;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.HumanPlayer.ActionType;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class ActionsKeys {
    public static int NO_KEY = -1;

    public static ActionsKeys DEFAULT_AS_ARROWS = new ActionsKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE);
    public static ActionsKeys DEFAULT_AS_WASD = new ActionsKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_TAB, KeyEvent.VK_ESCAPE);

    private int upKeyCode = NO_KEY;

    private int downKeyCode = NO_KEY;
    private int rightKeyCode = NO_KEY;
    private int leftKeyCode = NO_KEY;

    private int speedKeyCode = NO_KEY;
    private int exitKeyCode = NO_KEY;

    public ActionsKeys() {
    }

    public ActionsKeys(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode) {
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.rightKeyCode = rightKeyCode;
        this.leftKeyCode = leftKeyCode;
    }

    public ActionsKeys(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode, int speedKeyCode, int exitKeyCode) {
        this.upKeyCode = upKeyCode;
        this.downKeyCode = downKeyCode;
        this.rightKeyCode = rightKeyCode;
        this.leftKeyCode = leftKeyCode;
        this.speedKeyCode = speedKeyCode;
        this.exitKeyCode = exitKeyCode;
    }

    public boolean areAllDirectionsKeysSet() {
        return this.upKeyCode != ActionsKeys.NO_KEY &&
                this.downKeyCode != ActionsKeys.NO_KEY &&
                this.rightKeyCode != ActionsKeys.NO_KEY &&
                this.leftKeyCode != ActionsKeys.NO_KEY;
    }

    public boolean areAllKeysSet() {
        return this.areAllDirectionsKeysSet() &&
                this.speedKeyCode != ActionsKeys.NO_KEY &&
                this.exitKeyCode != ActionsKeys.NO_KEY;
    }


    public int getActionKeyCode(ActionType type) {
        ObjectAssertion.requireNonNull(type, "type can't be null");

        switch (type) {
            case UP:
                return upKeyCode;
            case RIGHT:
                return rightKeyCode;
            case DOWN:
                return downKeyCode;
            case LEFT:
                return leftKeyCode;
            case SPEED:
                return speedKeyCode;
            case EXIT:
                return exitKeyCode;
        }

        return NO_KEY;
    }

    public void setActionKeyCode(ActionType type , int keyCode) {
        ObjectAssertion.requireNonNull(type, "type can't be null");

        switch (type) {
            case UP:
                upKeyCode = keyCode;
                break;
            case RIGHT:
                rightKeyCode = keyCode;
                break;
            case DOWN:
                downKeyCode = keyCode;
                break;
            case LEFT:
                leftKeyCode = keyCode;
                break;
            case SPEED:
                speedKeyCode = keyCode;
                break;
            case EXIT:
                exitKeyCode = keyCode;
                break;
        }
    }

    public int getDirectionKeyCode(Direction direction) {
        ObjectAssertion.requireNonNull(direction, "Direction can't be null");

        switch (direction) {
            case UP:
                return upKeyCode;
            case RIGHT:
                return rightKeyCode;
            case DOWN:
                return downKeyCode;
            case LEFT:
                return leftKeyCode;
        }

        return NO_KEY;
    }

    public void setDirectionKeyCode(Direction direction , int keyCode) {
        ObjectAssertion.requireNonNull(direction, "Direction can't be null");

        switch (direction) {
            case UP:
                upKeyCode = keyCode;
                break;
            case RIGHT:
                rightKeyCode = keyCode;
                break;
            case DOWN:
                downKeyCode = keyCode;
                break;
            case LEFT:
                leftKeyCode = keyCode;
                break;
        }
    }

    public int getUpKeyCode() {
        return upKeyCode;
    }

    public void setUpKeyCode(int upKeyCode) {
        this.upKeyCode = upKeyCode;
    }

    public int getDownKeyCode() {
        return downKeyCode;
    }

    public void setDownKeyCode(int downKeyCode) {
        this.downKeyCode = downKeyCode;
    }

    public int getRightKeyCode() {
        return rightKeyCode;
    }

    public void setRightKeyCode(int rightKeyCode) {
        this.rightKeyCode = rightKeyCode;
    }

    public int getLeftKeyCode() {
        return leftKeyCode;
    }

    public void setLeftKeyCode(int leftKeyCode) {
        this.leftKeyCode = leftKeyCode;
    }

    public int getSpeedKeyCode() {
        return speedKeyCode;
    }

    public void setSpeedKeyCode(int speedKeyCode) {
        this.speedKeyCode = speedKeyCode;
    }

    public int getExitKeyCode() {
        return exitKeyCode;
    }

    public void setExitKeyCode(int exitKeyCode) {
        this.exitKeyCode = exitKeyCode;
    }

    public Map<Integer, Direction> convertToDirectionsKey() {
        Map<Integer, Direction> keyAssignment = new HashMap<>();

        keyAssignment.put(upKeyCode, Direction.UP);
        keyAssignment.put(downKeyCode, Direction.DOWN);
        keyAssignment.put(rightKeyCode, Direction.RIGHT);
        keyAssignment.put(leftKeyCode, Direction.LEFT);

        return keyAssignment;
    }
}
