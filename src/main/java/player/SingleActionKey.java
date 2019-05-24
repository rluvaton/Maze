package player;

import Helpers.SuccessCloneable;

import static GUI.KeyStrokeHelper.KeyStrokeUtils.getKeyText;

public class SingleActionKey implements SuccessCloneable<SingleActionKey> {
    private static final String NO_NAME = "None";
    public static int NO_KEY = -1;

    private int key = NO_KEY;
    private String keyName = "None";

    private final ActionType actionType;

    public SingleActionKey(ActionType actionType) {
        this.actionType = actionType;
    }

    public SingleActionKey(ActionType actionType, int key) {
        this(actionType, key, getKeyText(key));
    }

    public SingleActionKey(ActionType actionType, int key, String keyName) {
        this.key = key;
        this.keyName = keyName;
        this.actionType = actionType;
    }

    public boolean isKeySet() {
        return this.key != NO_KEY;
    }

    public int getKey() {
        return key;
    }

    public SingleActionKey removeKey() {
        key = NO_KEY;
        keyName = NO_NAME;
        return this;
    }

    public SingleActionKey setKey(int key) {
        this.key = key;
        if (key < 0) {
            return removeKey();
        } else {
            keyName = getKeyText(key);
        }
        return this;
    }

    public String getKeyName() {
        return keyName;
    }

    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public String toString() {
        return keyName;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public SingleActionKey clone() {
        return new SingleActionKey(actionType, key, keyName);
    }
}
