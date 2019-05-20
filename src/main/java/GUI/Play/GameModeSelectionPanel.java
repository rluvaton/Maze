package GUI.Play;

import Helpers.CallbackFns;
import Helpers.ThrowableAssertions.ObjectAssertion;

import javax.swing.*;
import java.awt.*;

public class GameModeSelectionPanel extends JPanel {

    private JButton customGameBtn;
    private JLabel header;
    private JPanel btnsContainer;
    private JButton stepModeBtn;

    private CallbackFns.ArgsVoidCallbackFunction<GameModes> onSelectedMode;

    public GameModeSelectionPanel(CallbackFns.ArgsVoidCallbackFunction<GameModes> onSelectedMode) {
        ObjectAssertion.requireNonNull(onSelectedMode, "On selected Mode callback function can't be null");

        this.onSelectedMode = onSelectedMode;
    }

    public void initUIComponents() {

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initHeaderLabel();

        initBtnsContainer();

        initListeners();
    }

    private void initBtnsContainer() {
        btnsContainer = new JPanel();

        initStepModeBtn();
        initCustomGameBtn();

        this.add(btnsContainer);
    }

    private void initCustomGameBtn() {
        customGameBtn = new JButton();
        customGameBtn.setText("Custom");
        btnsContainer.add(customGameBtn);
    }

    private void initStepModeBtn() {
        stepModeBtn = new JButton();
        stepModeBtn.setText("Steps");
        btnsContainer.add(stepModeBtn);
    }

    private void initHeaderLabel() {
        header = new JLabel();

        header.setText("Choose Game Creation Mode");

        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setMaximumSize(new Dimension(141, 40));
        header.setMinimumSize(new Dimension(141, 13));
        header.setPreferredSize(new Dimension(141, 40));

        this.add(header);
    }

    private void initListeners() {
        this.stepModeBtn.addActionListener((e) -> modeSelected(GameModes.STEPS));
        this.customGameBtn.addActionListener((e) -> modeSelected(GameModes.CUSTOM));
    }

    private void modeSelected(GameModes mode) {
        ObjectAssertion.requireNonNull(mode, "Mode can't be null");
        onSelectedMode.run(mode);
    }
}
