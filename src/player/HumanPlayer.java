package player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Helpers.Direction;
import Helpers.Tuple;

/**
 * Human Player
 *
 * @implNote Implement {@link KeyListener} for moving with the keyboard
 */
public class HumanPlayer extends BasePlayer implements KeyListener
{
    /**
     * Human Player Constructor
     *
     * @param location Starting location of the player
     */
    public HumanPlayer(Tuple<Integer, Integer> location) {
        super(location);
    }

    // region Base Player abstract functions

    /**
     * Move Top
     */
    @Override
    public void top() {
        this.notifyMoved(Direction.TOP);
    }

    /**
     * Move Right
     */
    @Override
    public void right() {
        this.notifyMoved(Direction.RIGHT);
    }

    /**
     * Move Bottom
     */
    @Override
    public void bottom() {
        this.notifyMoved(Direction.BOTTOM);
    }

    /**
     * Move Left
     */
    @Override
    public void left() {
        this.notifyMoved(Direction.LEFT);
    }

    // endregion

    // region KeyListener functions

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                this.top();
                break;
            case KeyEvent.VK_DOWN:
                this.bottom();
                break;
            case KeyEvent.VK_LEFT:
                this.left();
                break;
            case KeyEvent.VK_RIGHT:
                this.right();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // endregion
}
