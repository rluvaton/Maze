package GUI.Play;

import GUI.Play.Exceptions.NotFinishedStepException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SelectExitEntranceMinDistanceStep extends JPanel implements IPlayConfigStep {

    private JSpinner exitsCountValue;
    private JProgressBar stepsProgress;
    private JSpinner minDistanceValue;
    private JButton nextButton;
    private JSpinner entrancesCountValue;

    MazeGenerator.Builder builder;

    public SelectExitEntranceMinDistanceStep(MazeGenerator.Builder builder) {
        this.builder = builder;
    }

    public void init() {
        this.setLayout(new FormLayout("fill:p:noGrow,left:59dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:52px:grow,left:4dlu:noGrow,left:5dlu:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
//        stepPanel.add(selectMazeShape, "Card1");
    }

    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        initExitsCount(cc);
        initMinDistance(cc);
        initEntrancesCount(cc);

    }

    private void initEntrancesCount(CellConstraints cc) {
        final JLabel label4 = new JLabel();
        label4.setText("Entrances Count");
        this.add(label4, new CellConstraints(2, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));

        entrancesCountValue = new JSpinner();
        this.add(entrancesCountValue, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
    }

    private void initMinDistance(CellConstraints cc) {
        minDistanceValue = new JSpinner();
        this.add(minDistanceValue, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label3 = new JLabel();
        label3.setText("Min Distance");
        this.add(label3, new CellConstraints(1, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initExitsCount(CellConstraints cc) {
        exitsCountValue = new JSpinner();
        this.add(exitsCountValue, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label2 = new JLabel();
        label2.setText("Exits Count");
        this.add(label2, new CellConstraints(1, 3, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    @Override
    public boolean canContinue() {
        int mazeArea = this.builder.getWidth() * this.builder.getHeight();
        return this.validateMinDistance() &&
                (((int) this.entrancesCountValue.getValue()) + (int) this.exitsCountValue.getValue()) < mazeArea;
    }

    private boolean validateMinDistance() {
        int minDistance = (int) this.minDistanceValue.getValue();
        return minDistance <= 0 || minDistance < (this.builder.getHeight() * this.builder.getWidth());
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.SET_EXIT_ENTRANCES_MIN_DISTANCE;
    }

    @Override
    public MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        if (builder == null) {
            throw new NotFinishedStepException("builder is null", this);
        }

        return builder
                .setMinDistance((Integer) this.minDistanceValue.getValue())
                .setNumOfEntrance((Integer) this.entrancesCountValue.getValue())
                .setNumOfExits((Integer) this.exitsCountValue.getValue());

    }
}
