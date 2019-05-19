package GUI.Stats;

import GUI.Utils.GuiHelper;
import Statistics.GameStats;
import Statistics.User;
import Statistics.UsersDataSource;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class UsersStatPanel extends JPanel {
    public static String DEFAULT_CARD_NAME = "StatsCard";

    private JLabel header;

    private JLabel totalUsersPlayedValueLabel;
    private JLabel totalTimePlayedValueLabel;

    private JComboBox userSelection;
    private JLabel userSelectionLabel;

    private SingleUserStatPanel singleUserStatPanel;

    private GameStats stats;

    public UsersStatPanel() {
    }

    public void init() {
        this.setLayout(new FormLayout("fill:43px:noGrow,left:4dlu:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:42px:noGrow,left:4dlu:noGrow,fill:109px:noGrow,left:4dlu:noGrow,fill:65px:grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:8dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:106px:noGrow"));
    }

    public void initComponents() {
        CellConstraints cc = new CellConstraints();

        this.stats = UsersDataSource.getStats();

        initHeader(cc);

        initSingleUserStatPanel(cc);

        initTotalUsersPlayed(cc);

        initTotalTimePlayed(cc);

        initUserSelection(cc);

        initListeners();
    }

    private void initHeader(CellConstraints cc) {
        header = new JLabel();
        Font headerFont = GuiHelper.getFont("Source Code Pro", Font.BOLD, 18, header.getFont());
        if (headerFont != null) {
            header.setFont(headerFont);
        }
        header.setHorizontalAlignment(0);
        header.setText("Stats");
        this.add(header, cc.xyw(1, 1, 10));
    }

    private void initSingleUserStatPanel(CellConstraints cc) {
        singleUserStatPanel = new SingleUserStatPanel();

        // TODO - REMOVE
        singleUserStatPanel.init();

        this.add(singleUserStatPanel, cc.xywh(1, 7, 10, 3));

        // TODO - REMOVE
        singleUserStatPanel.initComponents();

    }

    private void initTotalUsersPlayed(CellConstraints cc) {
        totalUsersPlayedValueLabel = new JLabel();

        totalUsersPlayedValueLabel.setText("0");
        this.add(totalUsersPlayedValueLabel, cc.xy(6, 3));

        initLabelForValue(cc, "Total Users Played:", 4);
    }

    private void initTotalTimePlayed(CellConstraints cc) {
        initLabelForValue(cc, "Total Time Played:", 8);

        totalTimePlayedValueLabel = new JLabel();

        totalTimePlayedValueLabel.setText("0s");
        this.add(totalTimePlayedValueLabel, cc.xy(10, 3));
    }

    private void initLabelForValue(CellConstraints cc, String text, int col) {
        final JLabel label = new JLabel();

        label.setText(text);

        this.add(label, cc.xy(col, 3));
    }


    private void initUserSelection(CellConstraints cc) {
        userSelection = new JComboBox<User>();

        userSelection.setMinimumSize(new Dimension(150, 24));
        userSelection.setPreferredSize(new Dimension(150, 24));

        Collection<User> users = (this.stats == null) ? new LinkedList<>() : List.of(this.stats.getUsers());
        Vector<User> options = new Vector<>();

        // Add empty value
        options.add(null);

        // Add after the empty value all the values
        options.addAll(users);

        userSelection.setModel(new DefaultComboBoxModel<>(options));
        userSelection.setSelectedItem(0);

        this.add(userSelection, cc.xyw(6, 5, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

        userSelectionLabel = new JLabel();

        userSelectionLabel.setText("User");
        this.add(userSelectionLabel, new CellConstraints(4, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));

        userSelectionLabel.setLabelFor(userSelection);
    }


    private void initListeners() {
        listenToUserSelectionChange();
    }

    private void listenToUserSelectionChange() {
        this.userSelection.addItemListener(event -> {
            int state = event.getStateChange();
            User user = null;

            switch (state) {
                case ItemEvent.SELECTED:
                    user = (User) event.getItem();
                    break;
                case ItemEvent.DESELECTED:
                    break;
                default:
                    return;
            }

            this.singleUserStatPanel.setUser(user);
        });
    }

}
