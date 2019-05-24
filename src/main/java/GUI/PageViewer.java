package GUI;

import java.awt.*;
import java.util.Optional;

import static GUI.Utils.GuiHelper.getCurrentCard;

public class PageViewer extends CardLayout {

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Optional<Component> current = getCurrentCard(parent);
        if (current.isPresent() && current.get() instanceof WindowCard) {
            return ((WindowCard) current.get()).getPreferredSize();
        } else if (current.isPresent()) {
            Insets insets = parent.getInsets();
            Dimension pref = current.get().getPreferredSize();
            pref.width += insets.left + insets.right;
            pref.height += insets.top + insets.bottom;
            return pref;
        }

        return super.preferredLayoutSize(parent);
    }
}