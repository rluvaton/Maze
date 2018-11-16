package player;

import Helpers.Direction;
import Helpers.Tuple;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HumanPlayer extends BasePlayer implements KeyListener
{
    public HumanPlayer(Tuple<Integer, Integer> location) {
        super(location);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                this.top();
                // handle up
                break;
            case KeyEvent.VK_DOWN:
                this.bottom();
                // handle down
                break;
            case KeyEvent.VK_LEFT:
                this.left();
                // handle left
                break;
            case KeyEvent.VK_RIGHT :
                this.right();
                // handle right
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void top() {
        this.notifyMoved(Direction.TOP);
    }

    @Override
    public void right() {
        this.notifyMoved(Direction.RIGHT);

    }

    @Override
    public void bottom() {
        this.notifyMoved(Direction.BOTTOM);

    }

    @Override
    public void left() {
        this.notifyMoved(Direction.LEFT);
    }
}
