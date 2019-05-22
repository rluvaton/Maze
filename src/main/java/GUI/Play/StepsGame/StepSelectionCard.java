package GUI.Play.StepsGame;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Play.CustomGame.IPlayConfigStep;
import GUI.Play.CustomGame.PlayStep;
import Game.GameStep;
import Game.MazeGame;
import Helpers.ThrowableAssertions.ObjectAssertion;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class StepSelectionCard extends JPanel implements IPlayConfigStep {

    private JLabel header;
    private JComboBox<GameStep.BuiltinStep> stepsComboBox;

    private void initSteps() {
        stepsComboBox = new JComboBox<>();

        stepsComboBox.setModel(new DefaultComboBoxModel<>(GameStep.STEPS));
        stepsComboBox.setPreferredSize(new Dimension(81, 19));

        this.add(stepsComboBox);
    }

    private void initHeader() {
        header = new JLabel();
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setText("Choose Step");

        header.setAlignmentX(CENTER_ALIGNMENT);
        header.setHorizontalTextPosition(SwingConstants.CENTER);

        header.setMaximumSize(new Dimension(141, 40));
        header.setMinimumSize(new Dimension(141, 13));
        header.setPreferredSize(new Dimension(141, 40));

        this.add(header);
    }

    @Override
    public void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public void initComponents() {
        initHeader();
        initSteps();
    }

    @Override
    public void reset() {
        this.stepsComboBox.setSelectedIndex(0);
    }

    @Override
    public boolean canContinue() {
        return true;
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.STEP_SELECTION;
    }

    @Override
    public MazeGame.Builder appendData(MazeGame.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        ObjectAssertion.requireNonNull(builder, "Builder can't be null");

        return builder.setStep(((GameStep.BuiltinStep) Objects.requireNonNull(stepsComboBox.getSelectedItem())).getStep());
    }
}
