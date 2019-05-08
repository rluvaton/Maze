package player;

import Helpers.Coordinate;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Human Player
 *
 * @implNote Implement {@link KeyListener} for moving with the keyboard
 */
public class HumanPlayer extends BasePlayer implements KeyListener {

    private Map<Integer, Runnable> directionKeys = this.getDefaultKeyAssignment();

    public HumanPlayer(Coordinate startingLocation) {
        super(startingLocation);
    }

    public HumanPlayer(Coordinate startingLocation, String name) {
        super(startingLocation, name);
    }

    public HumanPlayer(Coordinate startingLocation, String name, DirectionKeys directionKeys) {
        super(startingLocation, name);

        assert directionKeys != null;
        this.directionKeys = createKeyAssignmentActions(directionKeys);
    }


    private Map<Integer, Runnable> getDefaultKeyAssignment() {
        return this.createKeyAssignmentActions(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
    }

    private Map<Integer, Runnable> createKeyAssignmentActions(DirectionKeys directionKeys) {
        assert directionKeys != null;
        return createKeyAssignmentActions(
                directionKeys.getUpKeyCode(),
                directionKeys.getDownKeyCode(),
                directionKeys.getRightKeyCode(),
                directionKeys.getLeftKeyCode()
        );
    }

    private Map<Integer, Runnable> createKeyAssignmentActions(int upKeyCode, int downKeyCode, int rightKeyCode, int leftKeyCode) {
        Map<Integer, Runnable> keyAssignment = new HashMap<>();

        keyAssignment.put(upKeyCode, this::up);
        keyAssignment.put(downKeyCode, this::down);
        keyAssignment.put(rightKeyCode, this::right);
        keyAssignment.put(leftKeyCode, this::left);

        return keyAssignment;
    }

    // region KeyListener functions

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!directionKeys.containsKey(keyCode)) {
            return;
        }

        directionKeys.get(keyCode).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // endregion
}
