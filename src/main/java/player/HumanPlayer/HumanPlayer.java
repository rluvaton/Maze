package player.HumanPlayer;

import GUI.Color;
import Helpers.CallbackFns.ArgsVoidCallbackFunction;
import Helpers.CallbackFns.NoArgsVoidCallbackFunction;
import Helpers.Coordinate;
import Helpers.Direction;
import Logger.LoggerManager;
import player.ActionsKeys;
import player.BasePlayer;
import player.exceptions.PlayerNotRunning;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Human Player
 *
 * @implNote Implement {@link KeyListener} for moving with the keyboard
 */
public class HumanPlayer extends BasePlayer implements KeyListener {
    private Map<Integer, ArgsVoidCallbackFunction<Boolean>> specialKeys = this.getDefaultSpecialKeyAssignment();
    private Map<Integer, NoArgsVoidCallbackFunction> specialTypedKeys = this.getDefaultSpecialTypedKeyAssignment();


    private Map<Integer, Direction> directionKeys = this.getDefaultKeyAssignment();
    private Map<Integer, Boolean> pressedKeys = new HashMap<>();

    private Thread playerThread = null;
    private RunnableHumanPlayer runnablePlayer = null;
    private int defaultStepSpeed = 200;
    private int defaultEnhancedStepSpeed = 100;

    private boolean currentlyOnPause = false;

    public HumanPlayer(Coordinate startingLocation) {
        super(startingLocation);
    }

    public HumanPlayer(Coordinate startingLocation, String name) {
        super(startingLocation, name);
    }

    public HumanPlayer(Coordinate startingLocation, String name, ActionsKeys directionActions) {
        super(startingLocation, name);

        assert directionActions != null;
        this.directionKeys = createKeyAssignment(directionActions);
        this.specialKeys = createSpecialKeyAssignment(directionActions);
    }

    public HumanPlayer(Coordinate startingLocation, String name, Color color, ActionsKeys directionActions) {
        super(startingLocation, name, color);
        this.directionKeys = createKeyAssignment(directionActions);
        this.specialKeys = createSpecialKeyAssignment(directionActions);
    }

    private Map<Integer, Direction> getDefaultKeyAssignment() {
        return this.createKeyAssignment(ActionsKeys.DEFAULT_AS_ARROWS);
    }

    private Map<Integer, Direction> createKeyAssignment(ActionsKeys actionsKeys) {
        assert actionsKeys != null;
        return actionsKeys.convertToDirectionsKey();
    }

    private Map<Integer, ArgsVoidCallbackFunction<Boolean>> getDefaultSpecialKeyAssignment() {
        return this.createSpecialKeyAssignment(ActionsKeys.DEFAULT_AS_ARROWS);
    }

    private Map<Integer, ArgsVoidCallbackFunction<Boolean>> createSpecialKeyAssignment(ActionsKeys actionsKeys) {
        assert actionsKeys != null;
        return createSpecialKeyAssignment(
                actionsKeys.getSpeedKeyCode()
        );
    }

    private Map<Integer, ArgsVoidCallbackFunction<Boolean>> createSpecialKeyAssignment(int speedKeyCode) {
        Map<Integer, ArgsVoidCallbackFunction<Boolean>> keyAssignment = new HashMap<>();

        keyAssignment.put(speedKeyCode, this::onSpeedKeyPressed);

        return keyAssignment;
    }


    private Map<Integer, NoArgsVoidCallbackFunction> getDefaultSpecialTypedKeyAssignment() {
        return this.createSpecialTypedKeyAssignment(ActionsKeys.DEFAULT_AS_ARROWS);
    }

    private Map<Integer, NoArgsVoidCallbackFunction> createSpecialTypedKeyAssignment(ActionsKeys actionsKeys) {
        assert actionsKeys != null;
        return createSpecialTypedKeyAssignment(
                actionsKeys.getExitKeyCode()
        );
    }

    private Map<Integer, NoArgsVoidCallbackFunction> createSpecialTypedKeyAssignment(int exitKeyCode) {
        Map<Integer, NoArgsVoidCallbackFunction> typedKeyAssignment = new HashMap<>();

        typedKeyAssignment.put(exitKeyCode, this::onExit);

        return typedKeyAssignment;
    }

    private void onSpeedKeyPressed(boolean isPressed) {
        if (isPlayerOperationForbidden()) {
            return;
        }

        assert runnablePlayer != null;

        runnablePlayer.pause();
        int stepSpeedMs = isPressed ? this.defaultEnhancedStepSpeed : this.defaultStepSpeed;
        runnablePlayer.setStepSpeedMs(stepSpeedMs);
        runnablePlayer.resume();
    }

    private void onExit() {
        if (runnablePlayer == null) {
            return;
        }

        // If already in pause mode resume, else pause
        onPlayerPauseSub.onNext(!this.currentlyOnPause);
    }

    public Thread create() {
        this.runnablePlayer = new RunnableHumanPlayer(this, defaultStepSpeed);
        this.playerThread = createPlayerThread();
        return this.playerThread;
    }

    private Thread createPlayerThread() {
        Thread playerThread = new Thread(runnablePlayer);
        playerThread.setName("Player " + this.getName() + " Thread");
        return playerThread;
    }

    // region KeyListener functions

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.put(keyCode, true);

        if (specialKeys.containsKey(keyCode)) {
            specialKeys.get(keyCode).run(true);
        }

        if (isPlayerOperationForbidden()) {
            return;
        }

        if (directionKeys.containsKey(keyCode)) {
            movePlayerToDirection(directionKeys.get(keyCode));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys.remove(keyCode);

        if (specialTypedKeys.containsKey(keyCode)) {
            specialTypedKeys.get(keyCode).run();
        }

        if (specialKeys.containsKey(keyCode)) {
            specialKeys.get(keyCode).run(false);
        }

        if (isPlayerOperationForbidden()) {
            return;
        }

        if (directionKeys.containsKey(keyCode)) {
            movePlayerToDirection(getActiveDirection());
        }

    }

    private boolean isPlayerOperationForbidden() {
        return this.runnablePlayer == null || currentlyOnPause;
    }

    private void movePlayerToDirection(Direction activeDirection) {
        this.runnablePlayer.pause();
        this.runnablePlayer.setDirection(activeDirection);
        this.runnablePlayer.resume();
    }

    private Direction getActiveDirection() {
        int currentlyPressedDirectionKey = directionKeys.keySet().stream().filter(pressedKeys::containsKey).findFirst().orElse(ActionsKeys.NO_KEY);

        return this.directionKeys.getOrDefault(currentlyPressedDirectionKey, null);
    }

    // endregion


    @Override
    protected void onPauseAction(Boolean isPause) {
        super.onPauseAction(isPause);
        this.currentlyOnPause = isPause;
    }

    @Override
    public void pause() throws PlayerNotRunning {
        if (runnablePlayer == null) {
            throw new PlayerNotRunning();
        }
        this.runnablePlayer.pause();
    }

    @Override
    public void resume() throws PlayerNotRunning {
        if (runnablePlayer == null) {
            throw new PlayerNotRunning();
        }
        this.runnablePlayer.resume();
    }

    @Override
    public void onPlayerFinished() {
        super.onPlayerFinished();
        LoggerManager.logger.info("[Player][onPlayerFinished]");

        if (this.runnablePlayer != null) {
            LoggerManager.logger.info("[Player Finish][Stopping Thread]");
            this.runnablePlayer.stop();

            this.runnablePlayer = null;
            this.playerThread = null;
        }
    }
}
