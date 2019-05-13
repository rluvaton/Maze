package GUI.MazeGame;

import java.awt.*;

@FunctionalInterface
public interface CreateArrowFn {
    void createArrow(Graphics g, int angle, int arrowX, int arrowY, boolean isEntrance);
}
