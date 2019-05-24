package GUI.Play.Shared.PlayerCreation;

import Helpers.CallbackFns;
import player.ActionsKeys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import static java.awt.event.KeyEvent.getKeyText;

public class HumanPlayerSettingPanel extends JPanel {

    private JTextField upIn;

    private JTextField rightIn;

    private JTextField downIn;

    private JTextField leftIn;

    private JTextField speedIncreaseKeyIn;

    private JTextField exitKeyIn;

    private ActionsKeys actionsKeys = new ActionsKeys();

    private JSlider humanSpeedSlider;

    // region init

    public void initUIComponents() {
        this.setLayout(new GridBagLayout());

        initDirectionKeys();
        initSpecialKeys();

        initHumanSpeed();

        initListeners();
    }

    private void initListeners() {
        initActionKeysListeners();
    }

    private void initActionKeysListeners() {

        // Lambada used and not method reference:
        // because we want that when the actions keys variable will change value it will the value in the new action keys

        this.downIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setDownKey(keyCode)));
        this.upIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setUpKey(keyCode)));
        this.leftIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setLeftKey(keyCode)));
        this.rightIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setRightKey(keyCode)));

        this.exitKeyIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setExitKey(keyCode)));
        this.speedIncreaseKeyIn.addKeyListener(new KeyActionListener(keyCode -> actionsKeys.setSpeedKey(keyCode)));

    }


    private void initSpecialKeys() {
        initSpeedKey();
        initExitKey();
    }

    private void initExitKey() {
        initExitKeyInput();
        initExitKeyLabel();
    }

    private void initExitKeyLabel() {

        JLabel exitKeyLabel = new JLabel();
        exitKeyLabel.setText("Exit Key");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 4;
        this.add(exitKeyLabel, gridBagConstraints);

    }

    private void initExitKeyInput() {
        exitKeyIn = new JTextField();
        exitKeyIn.setText("");
        setSizeOfKeyInput(exitKeyIn);


        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        this.add(exitKeyIn, gridBagConstraints);
    }

    private void initSpeedKey() {
        initSpeedKeyInput();
        initSpeedKeyLabel();
    }

    private void initSpeedKeyLabel() {

        JLabel speedIncreaseKeyLabel = new JLabel();
        speedIncreaseKeyLabel.setText("Speed Increase Key");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, 0, 4);
        this.add(speedIncreaseKeyLabel, gridBagConstraints);

    }

    private void initSpeedKeyInput() {

        speedIncreaseKeyIn = new JTextField();
        speedIncreaseKeyIn.setText("");
        setSizeOfKeyInput(speedIncreaseKeyIn);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 2;

        this.add(speedIncreaseKeyIn, gridBagConstraints);
    }

    private void initHumanSpeed() {
        initHumanSpeedSlider();
        initHumanSpeedLabel();
    }

    private void initHumanSpeedLabel() {

        JLabel playerSpeedLabel = new JLabel();
        playerSpeedLabel.setText("Player Speed");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new Insets(0, 0, 4, 0);
        this.add(playerSpeedLabel, gridBagConstraints);

    }

    private void initHumanSpeedSlider() {
        humanSpeedSlider = new JSlider(200, 1000, 300);

        humanSpeedSlider.setPaintLabels(true);
        humanSpeedSlider.setPaintTicks(true);
        humanSpeedSlider.setMajorTickSpacing(100);
        humanSpeedSlider.setSnapToTicks(true);

        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.insets = new Insets(0, 0, 4, 0);

        this.add(humanSpeedSlider, gridBagConstraints);

    }

    // region Init Direction Keys

    private void initDirectionKeys() {
        initTop();
        initRight();
        initBottom();
        initLeft();
    }


    private class KeyActionListener implements KeyListener {

        CallbackFns.ArgsVoidCallbackFunction<Integer> setKeyCode;

        int keyCode;
        int extendedKeyCode;

        String currentKeyText = null;

        public KeyActionListener(CallbackFns.ArgsVoidCallbackFunction<Integer> setKeyCode) {
            this.setKeyCode = setKeyCode;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getExtendedKeyCode() != extendedKeyCode) {
                return;
            }

            JTextField textField = (JTextField) e.getSource();

            if (!Objects.equals(textField.getText(), currentKeyText)) {
                textField.setText(currentKeyText);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyCode = e.getKeyCode();
            extendedKeyCode = e.getExtendedKeyCode();

            this.setKeyCode.run(keyCode);

            currentKeyText = getKeyText(keyCode);
            ((JTextField) e.getSource()).setText(currentKeyText);
        }
    }

    private void initBottom() {
        initBottomInput();
        initBottomLabel();
    }

    private void initBottomLabel() {
        GridBagConstraints gridBagConstraints;

        JLabel downLabel = new JLabel();
        downLabel.setText("Bottom");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(4, 0, 4, 0);

        this.add(downLabel, gridBagConstraints);
    }

    private void initBottomInput() {
        downIn = new JTextField();

        downIn.setText("");
        setSizeOfKeyInput(downIn);

        this.add(downIn, createGridBagConstraints(4, 5));
    }

    private void initTop() {
        initTopInput();
        initTopLabel();
    }

    private void initTopInput() {
        upIn = new JTextField();

        upIn.setText("");
        setSizeOfKeyInput(upIn);

        this.add(upIn, createGridBagConstraints(4, 1));
    }

    private GridBagConstraints createGridBagConstraints(int gridX, int gridY) {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridX;
        gridBagConstraints.gridy = gridY;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        return gridBagConstraints;
    }

    private void setSizeOfKeyInput(JTextField in) {
        Dimension size = new Dimension(50, 19);

        in.setMinimumSize(size);
        in.setPreferredSize(size);
        in.setMaximumSize(size);
    }

    private void initTopLabel() {
        GridBagConstraints gridBagConstraints;

        JLabel upLabel = new JLabel();
        upLabel.setText("Top");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new Insets(0, 0, 4, 0);

        this.add(upLabel, gridBagConstraints);
    }

    private void initRight() {
        initRightLabel();
        initRightInput();
    }

    private void initRightInput() {
        rightIn = new JTextField();

        rightIn.setText("");
        setSizeOfKeyInput(rightIn);

        this.add(rightIn, createGridBagConstraints(7, 3));
    }

    private void initRightLabel() {
        GridBagConstraints gridBagConstraints;

        JLabel rightLabel = new JLabel();
        rightLabel.setText("Right");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);

        this.add(rightLabel, gridBagConstraints);
    }

    private void initLeft() {
        initLeftIn();
        initLeftLabel();
    }

    private void initLeftIn() {
        leftIn = new JTextField();

        leftIn.setText("");
        setSizeOfKeyInput(leftIn);

        this.add(leftIn, createGridBagConstraints(1, 3));
    }

    private void initLeftLabel() {
        GridBagConstraints gridBagConstraints;

        JLabel leftLabel = new JLabel();
        leftLabel.setText("Left");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);

        this.add(leftLabel, gridBagConstraints);
    }

    // endregion

    // endregion

    public boolean canContinue() {
        return this.isPlayerWithCurrentInputsValid();
    }

    public ActionsKeys getActionsKeys() {
        return actionsKeys;
    }

    public int getPlayerSpeed() {
        return this.humanSpeedSlider.getValue();
    }

    public void reset() {

        this.upIn.setText("");
        this.downIn.setText("");
        this.rightIn.setText("");
        this.leftIn.setText("");

        this.exitKeyIn.setText("");
        this.speedIncreaseKeyIn.setText("");

        actionsKeys = new ActionsKeys();

        this.humanSpeedSlider.setValue(300);
    }

    public boolean isPlayerWithCurrentInputsValid() {
        return this.actionsKeys.areAllKeysSet();
    }

}
