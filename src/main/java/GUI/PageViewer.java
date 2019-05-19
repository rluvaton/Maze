package GUI;

import java.awt.*;

import static GUI.Utils.GuiHelper.findCurrentComponent;

public class PageViewer extends CardLayout {

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Component current = findCurrentComponent(parent);
        if (current instanceof WindowCard) {
            return ((WindowCard) current).getPreferredSize();
        } else if (current != null) {
            Insets insets = parent.getInsets();
            Dimension pref = current.getPreferredSize();
            pref.width += insets.left + insets.right;
            pref.height += insets.top + insets.bottom;
            return pref;
        }
        return super.preferredLayoutSize(parent);
    }
}