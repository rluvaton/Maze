package GUI.KeyStrokeHelper;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyStrokeNames {
    private static Map<Integer, String> keyStrokeNames = new HashMap<>();

    static {
        getKeyStrokeNamesMap();
    }

    private static void getKeyStrokeNamesMap() {
        addNumbersKeyStrokesToMap();
        addLettersKeyStrokesToMap();
        addNumPadNumericKeys();

        keyStrokeNames.put(KeyEvent.VK_COMMA, "COMMA");
        keyStrokeNames.put(KeyEvent.VK_PERIOD, "PERIOD");
        keyStrokeNames.put(KeyEvent.VK_SLASH, "SLASH");
        keyStrokeNames.put(KeyEvent.VK_SEMICOLON, "SEMICOLON");
        keyStrokeNames.put(KeyEvent.VK_EQUALS, "EQUALS");
        keyStrokeNames.put(KeyEvent.VK_OPEN_BRACKET, "OPEN_BRACKET");
        keyStrokeNames.put(KeyEvent.VK_BACK_SLASH, "BACK_SLASH");
        keyStrokeNames.put(KeyEvent.VK_CLOSE_BRACKET, "CLOSE_BRACKET");

        keyStrokeNames.put(KeyEvent.VK_ENTER, "ENTER");
        keyStrokeNames.put(KeyEvent.VK_BACK_SPACE, "BACK_SPACE");
        keyStrokeNames.put(KeyEvent.VK_TAB, "TAB");
        keyStrokeNames.put(KeyEvent.VK_CANCEL, "CANCEL");
        keyStrokeNames.put(KeyEvent.VK_CLEAR, "CLEAR");
        keyStrokeNames.put(KeyEvent.VK_SHIFT, "SHIFT");
        keyStrokeNames.put(KeyEvent.VK_CONTROL, "CONTROL");
        keyStrokeNames.put(KeyEvent.VK_ALT, "ALT");
        keyStrokeNames.put(KeyEvent.VK_PAUSE, "PAUSE");
        keyStrokeNames.put(KeyEvent.VK_CAPS_LOCK, "CAPS_LOCK");
        keyStrokeNames.put(KeyEvent.VK_ESCAPE, "ESCAPE");
        keyStrokeNames.put(KeyEvent.VK_SPACE, "SPACE");
        keyStrokeNames.put(KeyEvent.VK_PAGE_UP, "PAGE_UP");
        keyStrokeNames.put(KeyEvent.VK_PAGE_DOWN, "PAGE_DOWN");
        keyStrokeNames.put(KeyEvent.VK_END, "END");
        keyStrokeNames.put(KeyEvent.VK_HOME, "HOME");
        keyStrokeNames.put(KeyEvent.VK_LEFT, "LEFT");
        keyStrokeNames.put(KeyEvent.VK_UP, "UP");
        keyStrokeNames.put(KeyEvent.VK_RIGHT, "RIGHT");
        keyStrokeNames.put(KeyEvent.VK_DOWN, "DOWN");

        // Numpad numeric keys handled below
        keyStrokeNames.put(KeyEvent.VK_MULTIPLY, "MULTIPLY");
        keyStrokeNames.put(KeyEvent.VK_ADD, "ADD");
        keyStrokeNames.put(KeyEvent.VK_SEPARATOR, "SEPARATOR");
        keyStrokeNames.put(KeyEvent.VK_SUBTRACT, "SUBTRACT");
        keyStrokeNames.put(KeyEvent.VK_DECIMAL, "DECIMAL");
        keyStrokeNames.put(KeyEvent.VK_DIVIDE, "DIVIDE");
        keyStrokeNames.put(KeyEvent.VK_DELETE, "DELETE");
        keyStrokeNames.put(KeyEvent.VK_NUM_LOCK, "NUM_LOCK");
        keyStrokeNames.put(KeyEvent.VK_SCROLL_LOCK, "SCROLL_LOCK");

        keyStrokeNames.put(KeyEvent.VK_F1, "F1");
        keyStrokeNames.put(KeyEvent.VK_F2, "F2");
        keyStrokeNames.put(KeyEvent.VK_F3, "F3");
        keyStrokeNames.put(KeyEvent.VK_F4, "F4");
        keyStrokeNames.put(KeyEvent.VK_F5, "F5");
        keyStrokeNames.put(KeyEvent.VK_F6, "F6");
        keyStrokeNames.put(KeyEvent.VK_F7, "F7");
        keyStrokeNames.put(KeyEvent.VK_F8, "F8");
        keyStrokeNames.put(KeyEvent.VK_F9, "F9");
        keyStrokeNames.put(KeyEvent.VK_F10, "F10");
        keyStrokeNames.put(KeyEvent.VK_F11, "F11");
        keyStrokeNames.put(KeyEvent.VK_F12, "F12");
        keyStrokeNames.put(KeyEvent.VK_F13, "F13");
        keyStrokeNames.put(KeyEvent.VK_F14, "F14");
        keyStrokeNames.put(KeyEvent.VK_F15, "F15");
        keyStrokeNames.put(KeyEvent.VK_F16, "F16");
        keyStrokeNames.put(KeyEvent.VK_F17, "F17");
        keyStrokeNames.put(KeyEvent.VK_F18, "F18");
        keyStrokeNames.put(KeyEvent.VK_F19, "F19");
        keyStrokeNames.put(KeyEvent.VK_F20, "F20");
        keyStrokeNames.put(KeyEvent.VK_F21, "F21");
        keyStrokeNames.put(KeyEvent.VK_F22, "F22");
        keyStrokeNames.put(KeyEvent.VK_F23, "F23");
        keyStrokeNames.put(KeyEvent.VK_F24, "F24");

        keyStrokeNames.put(KeyEvent.VK_PRINTSCREEN, "PRINTSCREEN");
        keyStrokeNames.put(KeyEvent.VK_INSERT, "INSERT");
        keyStrokeNames.put(KeyEvent.VK_HELP, "HELP");
        keyStrokeNames.put(KeyEvent.VK_META, "META");
        keyStrokeNames.put(KeyEvent.VK_BACK_QUOTE, "BACK_QUOTE");
        keyStrokeNames.put(KeyEvent.VK_QUOTE, "QUOTE");

        keyStrokeNames.put(KeyEvent.VK_KP_UP, "KP_UP");
        keyStrokeNames.put(KeyEvent.VK_KP_DOWN, "KP_DOWN");
        keyStrokeNames.put(KeyEvent.VK_KP_LEFT, "KP_LEFT");
        keyStrokeNames.put(KeyEvent.VK_KP_RIGHT, "KP_RIGHT");

        keyStrokeNames.put(KeyEvent.VK_DEAD_GRAVE, "DEAD_GRAVE");
        keyStrokeNames.put(KeyEvent.VK_DEAD_ACUTE, "DEAD_ACUTE");
        keyStrokeNames.put(KeyEvent.VK_DEAD_CIRCUMFLEX, "DEAD_CIRCUMFLEX");
        keyStrokeNames.put(KeyEvent.VK_DEAD_TILDE, "DEAD_TILDE");
        keyStrokeNames.put(KeyEvent.VK_DEAD_MACRON, "DEAD_MACRON");
        keyStrokeNames.put(KeyEvent.VK_DEAD_BREVE, "DEAD_BREVE");
        keyStrokeNames.put(KeyEvent.VK_DEAD_ABOVEDOT, "DEAD_ABOVEDOT");
        keyStrokeNames.put(KeyEvent.VK_DEAD_DIAERESIS, "DEAD_DIAERESIS");
        keyStrokeNames.put(KeyEvent.VK_DEAD_ABOVERING, "DEAD_ABOVERING");
        keyStrokeNames.put(KeyEvent.VK_DEAD_DOUBLEACUTE, "DEAD_DOUBLEACUTE");
        keyStrokeNames.put(KeyEvent.VK_DEAD_CARON, "DEAD_CARON");
        keyStrokeNames.put(KeyEvent.VK_DEAD_CEDILLA, "DEAD_CEDILLA");
        keyStrokeNames.put(KeyEvent.VK_DEAD_OGONEK, "DEAD_OGONEK");
        keyStrokeNames.put(KeyEvent.VK_DEAD_IOTA, "DEAD_IOTA");
        keyStrokeNames.put(KeyEvent.VK_DEAD_VOICED_SOUND, "DEAD_VOICED_SOUND");
        keyStrokeNames.put(KeyEvent.VK_DEAD_SEMIVOICED_SOUND, "DEAD_SEMIVOICED_SOUND");

        keyStrokeNames.put(KeyEvent.VK_AMPERSAND, "AMPERSAND");
        keyStrokeNames.put(KeyEvent.VK_ASTERISK, "ASTERISK");
        keyStrokeNames.put(KeyEvent.VK_QUOTEDBL, "QUOTEDBL");
        keyStrokeNames.put(KeyEvent.VK_LESS, "LESS");
        keyStrokeNames.put(KeyEvent.VK_GREATER, "GREATER");
        keyStrokeNames.put(KeyEvent.VK_BRACELEFT, "BRACELEFT");
        keyStrokeNames.put(KeyEvent.VK_BRACERIGHT, "BRACERIGHT");
        keyStrokeNames.put(KeyEvent.VK_AT, "AT");
        keyStrokeNames.put(KeyEvent.VK_COLON, "COLON");
        keyStrokeNames.put(KeyEvent.VK_CIRCUMFLEX, "CIRCUMFLEX");
        keyStrokeNames.put(KeyEvent.VK_DOLLAR, "DOLLAR");
        keyStrokeNames.put(KeyEvent.VK_EURO_SIGN, "EURO_SIGN");
        keyStrokeNames.put(KeyEvent.VK_EXCLAMATION_MARK, "EXCLAMATION_MARK");
        keyStrokeNames.put(KeyEvent.VK_INVERTED_EXCLAMATION_MARK, "INVERTED_EXCLAMATION_MARK");
        keyStrokeNames.put(KeyEvent.VK_LEFT_PARENTHESIS, "LEFT_PARENTHESIS");
        keyStrokeNames.put(KeyEvent.VK_NUMBER_SIGN, "NUMBER_SIGN");
        keyStrokeNames.put(KeyEvent.VK_MINUS, "MINUS");
        keyStrokeNames.put(KeyEvent.VK_PLUS, "PLUS");
        keyStrokeNames.put(KeyEvent.VK_RIGHT_PARENTHESIS, "RIGHT_PARENTHESIS");
        keyStrokeNames.put(KeyEvent.VK_UNDERSCORE, "UNDERSCORE");

        keyStrokeNames.put(KeyEvent.VK_FINAL, "FINAL");
        keyStrokeNames.put(KeyEvent.VK_CONVERT, "CONVERT");
        keyStrokeNames.put(KeyEvent.VK_NONCONVERT, "NONCONVERT");
        keyStrokeNames.put(KeyEvent.VK_ACCEPT, "ACCEPT");
        keyStrokeNames.put(KeyEvent.VK_MODECHANGE, "MODECHANGE");
        keyStrokeNames.put(KeyEvent.VK_KANA, "KANA");
        keyStrokeNames.put(KeyEvent.VK_KANJI, "KANJI");
        keyStrokeNames.put(KeyEvent.VK_ALPHANUMERIC, "ALPHANUMERIC");
        keyStrokeNames.put(KeyEvent.VK_KATAKANA, "KATAKANA");
        keyStrokeNames.put(KeyEvent.VK_HIRAGANA, "HIRAGANA");
        keyStrokeNames.put(KeyEvent.VK_FULL_WIDTH, "FULL_WIDTH");
        keyStrokeNames.put(KeyEvent.VK_HALF_WIDTH, "HALF_WIDTH");
        keyStrokeNames.put(KeyEvent.VK_ROMAN_CHARACTERS, "ROMAN_CHARACTERS");
        keyStrokeNames.put(KeyEvent.VK_ALL_CANDIDATES, "ALL_CANDIDATES");
        keyStrokeNames.put(KeyEvent.VK_PREVIOUS_CANDIDATE, "PREVIOUS_CANDIDATE");
        keyStrokeNames.put(KeyEvent.VK_CODE_INPUT, "CODE_INPUT");
        keyStrokeNames.put(KeyEvent.VK_JAPANESE_KATAKANA, "JAPANESE_KATAKANA");
        keyStrokeNames.put(KeyEvent.VK_JAPANESE_HIRAGANA, "JAPANESE_HIRAGANA");
        keyStrokeNames.put(KeyEvent.VK_JAPANESE_ROMAN, "JAPANESE_ROMAN");
        keyStrokeNames.put(KeyEvent.VK_KANA_LOCK, "KANA_LOCK");
        keyStrokeNames.put(KeyEvent.VK_INPUT_METHOD_ON_OFF, "INPUT_METHOD_ON_OFF");

        keyStrokeNames.put(KeyEvent.VK_AGAIN, "AGAIN");
        keyStrokeNames.put(KeyEvent.VK_UNDO, "UNDO");
        keyStrokeNames.put(KeyEvent.VK_COPY, "COPY");
        keyStrokeNames.put(KeyEvent.VK_PASTE, "PASTE");
        keyStrokeNames.put(KeyEvent.VK_CUT, "CUT");
        keyStrokeNames.put(KeyEvent.VK_FIND, "FIND");
        keyStrokeNames.put(KeyEvent.VK_PROPS, "PROPS");
        keyStrokeNames.put(KeyEvent.VK_STOP, "STOP");

        keyStrokeNames.put(KeyEvent.VK_COMPOSE, "COMPOSE");
        keyStrokeNames.put(KeyEvent.VK_ALT_GRAPH, "ALT_GRAPH");

    }

    private static void addNumPadNumericKeys() {
        for (int keyCode = KeyEvent.VK_NUMPAD0; keyCode <= KeyEvent.VK_NUMPAD9; keyCode++) {
            char c = (char) (keyCode - KeyEvent.VK_NUMPAD0 + '0');
            keyStrokeNames.put(keyCode, "NUMPAD" + c);
        }
    }

    private static void addLettersKeyStrokesToMap() {
        for (int keyCode = KeyEvent.VK_A; keyCode <= KeyEvent.VK_Z; keyCode++) {
            addKeyStrokeFromInteger(keyStrokeNames, keyCode);
        }
    }

    private static void addNumbersKeyStrokesToMap() {
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_0);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_1);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_2);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_3);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_4);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_5);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_6);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_7);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_8);
        addKeyStrokeFromInteger(keyStrokeNames, KeyEvent.VK_9);
    }

    private static void addKeyStrokeFromInteger(Map<Integer, String> keyStrokes, int keyCode) {
        keyStrokes.put(keyCode, String.valueOf((char) keyCode));
    }

    public static String getKeyNameForKeyCode(int keyCode, String defaultName) {
        return keyStrokeNames.getOrDefault(keyCode, defaultName);
    }
}
