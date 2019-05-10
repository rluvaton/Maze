package GUI.Play;

import GUI.Play.Exceptions.NotFinishedStepException;
import Maze.Candy.Candy;
import Maze.Candy.CandyPowerType;
import Maze.MazeGenerator.GenerateCandyConfig;
import Maze.MazeGenerator.MazeGenerator;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class CandiesStep extends JPanel implements IPlayConfigStep {

    private JSpinner totalCandies;
    private JCheckBox candyPointsCheckBox;
    private JCheckBox onlyGoodCandiesCheckBox;
    private JCheckBox expiredCandiesCheckBox;
    private JCheckBox timeCandyCheckBox;
    private JCheckBox locationCandyCheckBox;

    public void init() {
        this.setLayout(new FormLayout("left:4dlu:noGrow,fill:max(d;4px):noGrow,fill:max(d;4px):noGrow,left:61dlu:noGrow,fill:57px:noGrow,left:4dlu:noGrow,fill:110px:noGrow,left:4dlu:noGrow,left:10dlu:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:13dlu:noGrow,center:max(d;4px):noGrow"));
    }

    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        initCandyPointsCheckBox();
        initTimeCandyCheckBox();
        initLocationCandyCheckBox();
        initExpiredCandiesCheckBox(cc);
        initOnlyGoodCandiesCheckBox(cc);
        initTotalCandies(cc);
    }

    private void initTotalCandies(CellConstraints cc) {
        final JLabel label2 = new JLabel();
        label2.setText("Total Candies");
        this.add(label2, cc.xywh(4, 6, 1, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));

        totalCandies = new JSpinner();
        this.add(totalCandies, new CellConstraints(5, 6, 3, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initOnlyGoodCandiesCheckBox(CellConstraints cc) {
        onlyGoodCandiesCheckBox = new JCheckBox();
        onlyGoodCandiesCheckBox.setText("Only Good Candies");
        this.add(onlyGoodCandiesCheckBox, cc.xyw(7, 1, 3));
    }

    private void initExpiredCandiesCheckBox(CellConstraints cc) {
        expiredCandiesCheckBox = new JCheckBox();
        expiredCandiesCheckBox.setText("Expired Candies");
        expiredCandiesCheckBox.setToolTipText("Candies that will expire after time");
        this.add(expiredCandiesCheckBox, cc.xyw(7, 3, 3));
    }

    private void initLocationCandyCheckBox() {
        locationCandyCheckBox = new JCheckBox();
        locationCandyCheckBox.setText("Location Candy");
        locationCandyCheckBox.setToolTipText("Teleport you to different location in the maze");
        this.add(locationCandyCheckBox, new CellConstraints(2, 5, 4, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initTimeCandyCheckBox() {
        timeCandyCheckBox = new JCheckBox();
        timeCandyCheckBox.setText("Time Candy");
        timeCandyCheckBox.setToolTipText("Candy that add you time or take from you time");
        this.add(timeCandyCheckBox, new CellConstraints(2, 3, 4, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initCandyPointsCheckBox() {
        candyPointsCheckBox = new JCheckBox();
        candyPointsCheckBox.setText("Candy Points");
        candyPointsCheckBox.setToolTipText("Candy that can give or take points");
        this.add(candyPointsCheckBox, new CellConstraints(2, 1, 4, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    @Override
    public boolean canContinue() {
        return true;
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.CANDIES_SELECTION;
    }

    @Override
    public MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        if (builder == null) {
            throw new NotFinishedStepException("builder is null", this);
        }

        LinkedList<CandyPowerType> candyTypes = new LinkedList<>();

        if (this.candyPointsCheckBox.isSelected()) {
            candyTypes.add(CandyPowerType.Points);
        }

        if (this.locationCandyCheckBox.isSelected()) {
            candyTypes.add(CandyPowerType.Location);
        }

        if (this.timeCandyCheckBox.isSelected()) {
            candyTypes.add(CandyPowerType.Time);
        }


        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setTypes(candyTypes.toArray(new CandyPowerType[0]));

        candyConfig.setStrengthPower(getStrengthPower());
        candyConfig.setTimeToLive(getTimeToLive());
        builder.setTotalCandies(getTotalCandiesValue());

        return builder
                .setCandyConfig(candyConfig);
    }

    private MazeGenerator.IntegerConfiguration getStrengthPower() {
        return this.onlyGoodCandiesCheckBox.isSelected() ? new MazeGenerator.IntegerConfiguration(1, 1000) : new MazeGenerator.IntegerConfiguration(0);
    }

    private MazeGenerator.IntegerConfiguration getTimeToLive() {
        return this.expiredCandiesCheckBox.isSelected() ? GenerateCandyConfig.DEFAULT_TIME_TO_LIVE : new MazeGenerator.IntegerConfiguration(Candy.WITHOUT_TIMEOUT);
    }

    private int getTotalCandiesValue() {
        Object totalCandiesValue = this.totalCandies.getValue();
        return totalCandiesValue instanceof Integer && (Integer) totalCandiesValue > 0 ? (Integer) totalCandiesValue : 0;
    }
}
