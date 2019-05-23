package GUI;

import java.awt.*;

public interface WindowCard {
    Dimension getPreferredSize();

    default boolean back() throws Exception {
        throw new Exception();
    }
}
