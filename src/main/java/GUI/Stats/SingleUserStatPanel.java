package GUI.Stats;

import GUI.GuiHelper;
import Helpers.TimeFormatter;
import Statistics.User;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SingleUserStatPanel extends JPanel {

    private User user;

    private JLabel totalGameValueLabel;
    private JLabel totalTimeLimitedGamesValueLabel;
    private JLabel maxMazeAreaPlayedValueLabel;
    private JLabel timePlayedValueLabel;
    private JLabel winningPercentageValueLabel;

    public SingleUserStatPanel() {
        // TODO - convert to user
    }
    
    public void init() {
        this.setLayout(new FormLayout("fill:63px:noGrow,left:4dlu:noGrow,fill:84px:noGrow,left:4dlu:noGrow,fill:158px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));

        this.setVisible(false);
    }

    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        initTotalGames(cc);
        initTotalTimeLimitedGames(cc);
        initMaxMazeAreaPlayed(cc);
        initTimePlayed(cc);
        initWinningPercentage(cc);
    }

    private void initWinningPercentage(CellConstraints cc) {
        winningPercentageValueLabel = new JLabel();

        winningPercentageValueLabel.setText("0");
        this.add(winningPercentageValueLabel, cc.xy(5, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));

        initLabelForInput("Winning Percentage", 9, 4);
    }

    private void initTimePlayed(CellConstraints cc) {
        timePlayedValueLabel = new JLabel();

        timePlayedValueLabel.setText("0s");
        this.add(timePlayedValueLabel, cc.xy(5, 7, CellConstraints.LEFT, CellConstraints.DEFAULT));

        initLabelForInput("Time Played", 7, 4);
    }

    private void initMaxMazeAreaPlayed(CellConstraints cc) {
        maxMazeAreaPlayedValueLabel = new JLabel();

        maxMazeAreaPlayedValueLabel.setText("0");
        this.add(maxMazeAreaPlayedValueLabel, cc.xy(5, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));

        initLabelForInput("Max Maze Area Played", 5, 4);
    }

    private void initTotalTimeLimitedGames(CellConstraints cc) {
        totalTimeLimitedGamesValueLabel = new JLabel();

        totalTimeLimitedGamesValueLabel.setText("0");
        this.add(totalTimeLimitedGamesValueLabel, cc.xy(5, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

        initLabelForInput("Total Time Limited Games", 3, 4);
    }

    private void initTotalGames(CellConstraints cc) {
        totalGameValueLabel = new JLabel();

        totalGameValueLabel.setText("0");
        this.add(totalGameValueLabel, cc.xy(5, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        initLabelForInput("Total Games", 1, 3);
    }

    private void initLabelForInput(String text, int gridY, int gridWidth) {
        final JLabel label = new JLabel();

        Font label5Font = GuiHelper.getFont(null, Font.BOLD, -1, label.getFont());
        if (label5Font != null) {
            label.setFont(label5Font);
        }

        label.setText(text);

        this.add(label, new CellConstraints(1, gridY, gridWidth, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    public void setUser(User user) {
        this.user = user;

        this.setVisible(this.user != null);
        this.updateComponentsValue();
    }

    private void updateComponentsValue() {
        if(this.user == null) {
            return;
        }

        totalGameValueLabel.setText(this.user.getTotalGames() + "");
        totalTimeLimitedGamesValueLabel.setText(this.user.getTotalTimeLimitedGames() + "");
        this.maxMazeAreaPlayedValueLabel.setText(this.user.getMaxMazeAreaPlayed() + "");

        this.timePlayedValueLabel.setText(TimeFormatter.convertFromSecondsToHumanReadableText(user.getTotalTimePlayedInSeconds()));
        this.winningPercentageValueLabel.setText(this.user.getWinningPercentage() + "%");

    }

}
