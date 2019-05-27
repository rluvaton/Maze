package GUI.KeyStrokeHelper;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class KeyStrokeUtils {

    public static String keyStroke2String(KeyStroke key) {
        if (key == null) {
            return "";
        }
        StringBuilder s = new StringBuilder(50);
        int m = key.getModifiers();

        if ((m & (InputEvent.CTRL_DOWN_MASK)) != 0) {
            s.append("Ctrl+");
        }
        if ((m & (InputEvent.META_DOWN_MASK)) != 0) {
            s.append("Meta+");
        }
        if ((m & (InputEvent.ALT_DOWN_MASK)) != 0) {
            s.append("Alt+");
        }
        if ((m & (InputEvent.SHIFT_DOWN_MASK)) != 0) {
            s.append("Shift+");
        }
        if ((m & (InputEvent.BUTTON1_DOWN_MASK)) != 0) {
            s.append("Button1+");
        }
        if ((m & (InputEvent.BUTTON2_DOWN_MASK)) != 0) {
            s.append("Button2+");
        }
        if ((m & (InputEvent.BUTTON3_DOWN_MASK)) != 0) {
            s.append("Button3+");
        }

        switch (key.getKeyEventType()) {
            case KeyEvent.KEY_TYPED:
                s.append(key.getKeyChar()).append(" ");
                break;
            case KeyEvent.KEY_PRESSED:
            case KeyEvent.KEY_RELEASED:
                s.append(getKeyText(key.getKeyCode())).append(" ");
                break;
            default:
                s.append("unknown-event-type ");
                break;
        }

        return s.toString();
    }

    public static String getKeyText(int keyCode) {
        String defaultName = "unknown(0x" + Integer.toString(keyCode, 16) + ")";
        return KeyStrokeNames.getKeyNameForKeyCode(keyCode, defaultName);
    }
}
