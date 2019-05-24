package player;

import Helpers.Direction;
import Helpers.SuccessCloneable;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.HumanPlayer.ActionType;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class ActionsKeys implements SuccessCloneable<ActionsKeys> {
    public static int NO_KEY = -1;

    public static ActionsKeys DEFAULT_AS_ARROWS = new ActionsKeys(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE);
    public static ActionsKeys DEFAULT_AS_WASD = new ActionsKeys(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_A, KeyEvent.VK_TAB, KeyEvent.VK_ESCAPE);

    private SingleActionKey upKey = new SingleActionKey(player.ActionType.UP);
    private SingleActionKey downKey = new SingleActionKey(player.ActionType.DOWN);
    private SingleActionKey rightKey = new SingleActionKey(player.ActionType.RIGHT);
    private SingleActionKey leftKey = new SingleActionKey(player.ActionType.LEFT);

    private SingleActionKey speedKey = new SingleActionKey(player.ActionType.SPEED);
    private SingleActionKey exitKey = new SingleActionKey(player.ActionType.EXIT);

    public ActionsKeys() {
    }

    public ActionsKeys(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode) {
        this.upKey.setKey(upKeyCode);
        this.downKey.setKey(downKeyCode);
        this.rightKey.setKey(rightKeyCode);
        this.leftKey.setKey(leftKeyCode);
    }

    public ActionsKeys(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode, int speedKeyCode, int exitKeyCode) {
        this.upKey.setKey(upKeyCode);
        this.downKey.setKey(downKeyCode);
        this.rightKey.setKey(rightKeyCode);
        this.leftKey.setKey(leftKeyCode);
        this.speedKey.setKey(speedKeyCode);
        this.exitKey.setKey(exitKeyCode);
    }

    public ActionsKeys(SingleActionKey upKey, SingleActionKey downKey, SingleActionKey rightKey, SingleActionKey leftKey, SingleActionKey speedKey, SingleActionKey exitKey) {
        this.upKey = upKey;
        this.downKey = downKey;
        this.rightKey = rightKey;
        this.leftKey = leftKey;
        this.speedKey = speedKey;
        this.exitKey = exitKey;
    }

    public boolean areAllDirectionsKeysSet() {
        return this.upKey.isKeySet() &&
                this.downKey.isKeySet() &&
                this.rightKey.isKeySet() &&
                this.leftKey.isKeySet();
    }

    public boolean areAllKeysSet() {
        return this.areAllDirectionsKeysSet() &&
                this.speedKey.isKeySet() &&
                this.exitKey.isKeySet();
    }


    public SingleActionKey getActionKey(ActionType type) {
        ObjectAssertion.requireNonNull(type, "type can't be null");

        switch (type) {
            case UP:
                return upKey;
            case RIGHT:
                return rightKey;
            case DOWN:
                return downKey;
            case LEFT:
                return leftKey;
            case SPEED:
                return speedKey;
            case EXIT:
                return exitKey;
        }

        return null;
    }

    public void setActionKeyCode(ActionType type , int keyCode) {
        ObjectAssertion.requireNonNull(type, "type can't be null");

        SingleActionKey actionKey = this.getActionKey(type);
        if(actionKey != null) {
            actionKey.setKey(keyCode);
        }
    }

    public SingleActionKey getActionKey(Direction direction) {
        ObjectAssertion.requireNonNull(direction, "Direction can't be null");

        switch (direction) {
            case UP:
                return upKey;
            case RIGHT:
                return rightKey;
            case DOWN:
                return downKey;
            case LEFT:
                return leftKey;
        }

        return null;
    }

    public void setActionKeyCode(Direction direction , int keyCode) {
        ObjectAssertion.requireNonNull(direction, "Direction can't be null");

        SingleActionKey actionKey = this.getActionKey(direction);

        if(actionKey != null) {
            actionKey.setKey(keyCode);
        }
    }

    public SingleActionKey getUpKey() {
        return upKey;
    }

    public ActionsKeys setUpKey(int upKey) {
        this.upKey.setKey(upKey);
        return this;
    }

    public SingleActionKey getDownKey() {
        return downKey;
    }

    public ActionsKeys setDownKey(int downKey) {
        this.downKey.setKey(downKey);
        return this;
    }

    public SingleActionKey getRightKey() {
        return rightKey;
    }

    public ActionsKeys setRightKey(int rightKey) {
        this.rightKey.setKey(rightKey);
        return this;
    }

    public SingleActionKey getLeftKey() {
        return leftKey;
    }

    public ActionsKeys setLeftKey(int leftKey) {
        this.leftKey.setKey(leftKey);
        return this;
    }

    public SingleActionKey getSpeedKey() {
        return speedKey;
    }

    public ActionsKeys setSpeedKey(int speedKey) {
        this.speedKey.setKey(speedKey);
        return this;
    }

    public SingleActionKey getExitKey() {
        return exitKey;
    }

    public ActionsKeys setExitKey(int exitKey) {
        this.exitKey.setKey(exitKey);
        return this;
    }

    public Map<Integer, Direction> convertToDirectionsKey() {
        Map<Integer, Direction> keyAssignment = new HashMap<>();

        keyAssignment.put(upKey.getKey(), Direction.UP);
        keyAssignment.put(downKey.getKey(), Direction.DOWN);
        keyAssignment.put(rightKey.getKey(), Direction.RIGHT);
        keyAssignment.put(leftKey.getKey(), Direction.LEFT);

        return keyAssignment;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ActionsKeys clone() {
        return new ActionsKeys(
                upKey.clone(),
                downKey.clone(),
                rightKey.clone(),
                leftKey.clone(),
                speedKey.clone(),
                exitKey.clone()
        );
    }
}
