package GUI.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class GuiHelper {

    public static Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) {
            return null;
        }
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * Create Spinner Model that his values are 1 <= x <= <code>Short.MAX_VALUE</code>
     */
    public static SpinnerNumberModel createSpinnerModelForPositiveNumberOnly() {
        return createSpinnerModelForPositiveNumberOnly(false, Short.MAX_VALUE);
    }

    /**
     * Create Spinner Model that his values are (0 or 1 [depends on the value of <code>includeZero</code>]) <= x <= <code>Short.MAX_VALUE</code>
     */
    public static SpinnerNumberModel createSpinnerModelForPositiveNumberOnly(boolean includeZero) {
        return createSpinnerModelForPositiveNumberOnly(includeZero, Short.MAX_VALUE);
    }

    /**
     * Create Spinner Model that his values are (0 or 1 [depends on the value of <code>includeZero</code>]) <= x <= <code>maxValue</code>
     */
    public static SpinnerNumberModel createSpinnerModelForPositiveNumberOnly(boolean includeZero, long maxValue) {
        int minValue = includeZero ? 0 : 1;
        //noinspection UnnecessaryBoxing
        return new SpinnerNumberModel(minValue, minValue, Math.toIntExact(maxValue), (int)Integer.valueOf(1));
    }

    public static Optional<Component> getCurrentCard(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (comp.isVisible()) {
                return Optional.of(comp);
            }
        }
        return Optional.empty();
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
