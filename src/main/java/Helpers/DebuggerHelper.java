package Helpers;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class DebuggerHelper implements KeyListener {
    private static DebuggerHelper instance;

    private volatile static boolean debugMode = false;

    private volatile Subject<KeyEvent> keyPressedSub = PublishSubject.create();

    private DebuggerHelper() {
    }

    public static DebuggerHelper getInstance() {
        instance = (instance == null) ? new DebuggerHelper() : instance;
        return instance;
    }

    // region KeyListener functions

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedSub.onNext(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // endregion

    public Observable<KeyEvent> getAllPressedKeyObs() {
        return keyPressedSub;
    }

    public Observable<KeyEvent> getSingleKeyPressedObs(int keyCode) {
        return keyPressedSub.filter(keyEvent -> keyEvent.getKeyCode() == keyCode);
    }

    public Observable<KeyEvent> getMultiKeyPressedObs(int[] allKeyCodes) {
        assert allKeyCodes != null;
        Set<Integer> keyCodes = new HashSet<>();

        for (int keyCode : allKeyCodes) {
            keyCodes.add(keyCode);
        }

        return keyPressedSub.filter(keyEvent -> keyCodes.contains(keyEvent.getKeyCode()));
    }

    public static boolean isInDebugMode() {
        return debugMode;
    }

    public static void setDebugMode(boolean debugMode) {
        DebuggerHelper.debugMode = debugMode;

        if(DebuggerHelper.debugMode) {
            System.out.println("App in debug mode");
        }
    }

}
