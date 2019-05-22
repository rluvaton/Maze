package GUI.Play.CustomGame;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Utils.GuiHelper;
import Game.MazeGame;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Maze.MazeGenerator.MazeGenerator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SelectExitEntranceMinDistanceStep extends JPanel implements IPlayConfigStep {

    private JSpinner exitsCountValue;
    private JSpinner minDistanceValue;
    private JSpinner entrancesCountValue;

    MazeGame.Builder builder;

    public SelectExitEntranceMinDistanceStep(MazeGame.Builder builder) {
        this.builder = builder;
    }

    public void init() {
        this.setLayout(new FormLayout("fill:p:noGrow,left:59dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:52px:grow,left:4dlu:noGrow,left:5dlu:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
    }

    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        initExitsCount(cc);
        initMinDistance(cc);
        initEntrancesCount(cc);

        this.setPreferredSize(null);
    }

    private void initEntrancesCount(CellConstraints cc) {
        final JLabel label4 = new JLabel();
        label4.setText("Entrances Count");
        this.add(label4, new CellConstraints(2, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));

        entrancesCountValue = new JSpinner(GuiHelper.createSpinnerModelForPositiveNumberOnly(true));
        this.add(entrancesCountValue, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    private void initMinDistance(CellConstraints cc) {
        minDistanceValue = new JSpinner(GuiHelper.createSpinnerModelForPositiveNumberOnly(true));
        this.add(minDistanceValue, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label3 = new JLabel();
        label3.setText("Min Distance");
        this.add(label3, new CellConstraints(1, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initExitsCount(CellConstraints cc) {
        exitsCountValue = new JSpinner(GuiHelper.createSpinnerModelForPositiveNumberOnly(true));
        this.add(exitsCountValue, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label2 = new JLabel();
        label2.setText("Exits Count");
        this.add(label2, new CellConstraints(1, 3, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    @Override
    public boolean canContinue() {
        if (builder == null) {
            return false;
        }
        MazeGenerator.Builder mazeGeneratorBuilder = builder.getMazeGeneratorBuilder();

        if (mazeGeneratorBuilder == null) {
            return false;
        }

        int width = mazeGeneratorBuilder.getWidth();
        int height = mazeGeneratorBuilder.getHeight();

        return this.validateMinDistance(width, height) &&
                (((int) this.entrancesCountValue.getValue()) + (int) this.exitsCountValue.getValue()) < width * height;
    }

    private boolean validateMinDistance(int width, int height) {
        int minDistance = (int) this.minDistanceValue.getValue();
        return minDistance <= 0 || minDistance < (height * width);
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.SET_EXIT_ENTRANCES_MIN_DISTANCE;
    }

    @Override
    public MazeGame.Builder appendData(MazeGame.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        ObjectAssertion.requireNonNull(builder, "builder can't be null");
        ObjectAssertion.requireNonNull(builder.getMazeGeneratorBuilder(), "mazeGeneratorBuilder can't be null");

        builder
                .getMazeGeneratorBuilder()
                .setMinDistance((Integer) this.minDistanceValue.getValue())
                .setNumOfEntrance((Integer) this.entrancesCountValue.getValue())
                .setNumOfExits((Integer) this.exitsCountValue.getValue());

        return builder;
    }

    @Override
    public void reset() {
        exitsCountValue.setValue(0);
        minDistanceValue.setValue(0);
        entrancesCountValue.setValue(0);
    }

    public void setBuilder(MazeGame.Builder builder) {
        this.builder = builder;
    }
}
