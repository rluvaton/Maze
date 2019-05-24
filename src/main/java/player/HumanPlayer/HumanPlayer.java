package player.HumanPlayer;

import GUI.Color;
import Helpers.CallbackFns.ArgsVoidCallbackFunction;
import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Helpers.Utils;
import Logger.LoggerManager;
import player.ActionsKeys;
import player.BasePlayer;
import player.exceptions.PlayerNotRunning;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Human Player
 *
 * @implNote Implement {@link KeyListener} for moving with the keyboard
 */
public class HumanPlayer extends BasePlayer implements KeyListener {

    private static final int DEFAULT_STEP_SPEED = 300;
    private static final int DEFAULT_ENHANCED_STEP_SPEED = 100;

    private ActionsKeys actionsKeys = ActionsKeys.DEFAULT_AS_ARROWS;
    private AbstractMap<Integer, ActionKey> specialKeys;
    private AbstractMap<Integer, ActionKey> specialTypedKeys;


    private Map<Integer, Direction> directionKeys = this.getDefaultKeyAssignment();
    private Map<Integer, Boolean> pressedKeys = new HashMap<>();

    private Thread playerThread = null;
    private RunnableHumanPlayer runnablePlayer = null;


    private int stepSpeed = DEFAULT_STEP_SPEED;
    private int enhancedStepSpeed = DEFAULT_ENHANCED_STEP_SPEED;

    private boolean currentlyOnPause = false;

    public HumanPlayer(Coordinate startingLocation) {
        super(startingLocation);

        createAllKeyAssignmentFromActionsKeys(actionsKeys);
    }


    public HumanPlayer(Coordinate startingLocation, String name) {
        super(startingLocation, name);

        createAllKeyAssignmentFromActionsKeys(actionsKeys);
    }

    public HumanPlayer(Coordinate startingLocation, String name, ActionsKeys actionsKeys) {
        super(startingLocation, name);

        createAllKeyAssignmentFromActionsKeys(actionsKeys);
    }

    public HumanPlayer(Coordinate startingLocation, String name, Color color, ActionsKeys actionsKeys) {
        this(startingLocation, name, color, DEFAULT_STEP_SPEED, actionsKeys);
    }

    public HumanPlayer(Coordinate startingLocation, String name, Color color, int stepSpeed, ActionsKeys actionsKeys) {
        this(startingLocation, name, color, stepSpeed, DEFAULT_ENHANCED_STEP_SPEED, actionsKeys);
    }

    public HumanPlayer(Coordinate startingLocation, String name, Color color, int stepSpeed, int enhancedStepSpeed, ActionsKeys actionsKeys) {
        super(startingLocation, name, color);

        this.stepSpeed = stepSpeed;
        this.enhancedStepSpeed = enhancedStepSpeed;

        createAllKeyAssignmentFromActionsKeys(actionsKeys);
    }

    private void createAllKeyAssignmentFromActionsKeys(ActionsKeys actionsKeys) {
        this.actionsKeys = actionsKeys;

        this.directionKeys = createKeyAssignment(actionsKeys);
        this.specialKeys = createSpecialKeyAssignment(actionsKeys);
        specialTypedKeys = createSpecialTypedKeyAssignment(actionsKeys);
    }

    private Map<Integer, Direction> getDefaultKeyAssignment() {
        return this.createKeyAssignment(this.actionsKeys);
    }

    private Map<Integer, Direction> createKeyAssignment(ActionsKeys actionsKeys) {
        ObjectAssertion.requireNonNull(actionsKeys, "Actions Keys can't be null");
        return actionsKeys.convertToDirectionsKey();
    }

    private AbstractMap<Integer, ActionKey> getDefaultSpecialKeyAssignment() {
        return this.createSpecialKeyAssignment(this.actionsKeys);
    }

    private AbstractMap<Integer, ActionKey> createSpecialKeyAssignment(ActionsKeys actionsKeys) {
        ObjectAssertion.requireNonNull(actionsKeys, "Actions Keys can't be null");
        return createSpecialKeyAssignment(
                actionsKeys.getSpeedKey().getKey()
        );
    }

    private AbstractMap<Integer, ActionKey> createSpecialKeyAssignment(int speedKeyCode) {
        AbstractMap<Integer, ActionKey> keyAssignment = new HashMap<>();

        keyAssignment.put(speedKeyCode, new ActionKey(ActionType.SPEED, this::onSpeedKeyPressed));

        return keyAssignment;
    }


    private AbstractMap<Integer, ActionKey> getDefaultSpecialTypedKeyAssignment() {
        return this.createSpecialTypedKeyAssignment(this.actionsKeys);
    }

    private AbstractMap<Integer, ActionKey> createSpecialTypedKeyAssignment(ActionsKeys actionsKeys) {
        assert actionsKeys != null;
        return createSpecialTypedKeyAssignment(
                actionsKeys.getExitKey().getKey()
        );
    }

    private AbstractMap<Integer, ActionKey> createSpecialTypedKeyAssignment(int exitKeyCode) {
        AbstractMap<Integer, ActionKey> typedKeyAssignment = new HashMap<>();

        typedKeyAssignment.put(exitKeyCode, new ActionKey(ActionType.EXIT, t -> onExit()));

        return typedKeyAssignment;
    }

    private void onSpeedKeyPressed(boolean isPressed) {
        if (isPlayerOperationForbidden()) {
            return;
        }

        runnablePlayer.pause();
        int stepSpeedMs = isPressed ? this.enhancedStepSpeed : this.stepSpeed;
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
        this.runnablePlayer = new RunnableHumanPlayer(this, stepSpeed);
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
            specialTypedKeys.get(keyCode).run(false);
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
            this.runnablePlayer.resume();

            this.runnablePlayer = null;
            this.playerThread = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        HumanPlayer humanPlayer = (HumanPlayer) o;
        return stepSpeed == humanPlayer.stepSpeed &&
                enhancedStepSpeed == humanPlayer.enhancedStepSpeed &&
                currentlyOnPause == humanPlayer.currentlyOnPause &&
                specialKeys.equals(humanPlayer.specialKeys) &&
                specialTypedKeys.equals(humanPlayer.specialTypedKeys) &&
                directionKeys.equals(humanPlayer.directionKeys) &&
                pressedKeys.equals(humanPlayer.pressedKeys) &&
                Objects.equals(playerThread, humanPlayer.playerThread) &&
                Objects.equals(runnablePlayer, humanPlayer.runnablePlayer);
    }

    @Override
    public BasePlayer clone() {
        return new HumanPlayer(Utils.clone(getLocation()), getName(), getColor(), this.stepSpeed, this.enhancedStepSpeed, this.actionsKeys.clone());
    }

    public int getStepSpeed() {
        return stepSpeed;
    }

    public ActionsKeys getActionsKeys() {
        return actionsKeys.clone();
    }

    private class ActionKey {
        private ActionType type;
        private ArgsVoidCallbackFunction<Boolean> action;

        public ActionKey(ActionType type, ArgsVoidCallbackFunction<Boolean> action) {
            this.type = type;
            this.action = action;
        }

        public ActionType getType() {
            return type;
        }

        void run(boolean value) {
            action.run(value);
        }

    }
}
