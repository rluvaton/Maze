package GUI.Play.Shared.PlayerCreation;

import GUI.Color;
import Helpers.CallbackFns;
import Helpers.ThrowableAssertions.ObjectAssertion;
import com.github.javafaker.Faker;
import player.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BasePlayerCreationPanel extends JPanel {

    private JButton createPlayerBtn;

    private JLabel playerTypeLabel;
    private JComboBox<PlayerType> playerTypeComboBox;

    private JTextField nameIn;
    private JLabel nameLabel;

    private JLabel colorLabel;
    private JComboBox<Color> colorComboBox;

    private CallbackFns.ArgsVoidCallbackFunction<PlayerType> onPlayerTypeSelected;
    private CallbackFns.NoArgsVoidCallbackFunction onCreatePlayerBtn;

    public BasePlayerCreationPanel(CallbackFns.ArgsVoidCallbackFunction<PlayerType> onPlayerTypeSelected,
                                   CallbackFns.NoArgsVoidCallbackFunction onCreatePlayerBtnClicked) {
        ObjectAssertion.requireNonNull(onPlayerTypeSelected, "`onPlayerTypeSelected` Can't be null");
        ObjectAssertion.requireNonNull(onCreatePlayerBtnClicked, "`onCreatePlayerBtnClicked` Can't be null");

        this.onPlayerTypeSelected = onPlayerTypeSelected;
        this.onCreatePlayerBtn = onCreatePlayerBtnClicked;
    }

    // region Init

    public void initUIComponents() {
        this.setLayout(new GridBagLayout());

        initPlayerType();
        initColor();
        initName();

        initCreatePlayerBtn();

        initListeners();
    }

    private void initListeners() {
        initListenerForCreatePlayerBtn();

        initListenerForSelectingPlayerType();
    }

    private void initListenerForSelectingPlayerType() {
        this.playerTypeComboBox.addItemListener(e -> {
            Object selectedPlayerType = playerTypeComboBox.getSelectedItem();
            if (selectedPlayerType != null) {
                onPlayerTypeSelected.run((PlayerType) selectedPlayerType);
            }
        });
    }

    private void initListenerForCreatePlayerBtn() {
        this.createPlayerBtn.addActionListener(this::onCreatePlayerBtnClicked);
    }

    private void onCreatePlayerBtnClicked(ActionEvent e) {
        this.onCreatePlayerBtn.run();
        this.resetOnPlayerCreated();
    }

    private void resetOnPlayerCreated() {
        this.playerTypeComboBox.setSelectedItem(PlayerType.HUMAN);

        nameIn.setText("");

        colorComboBox.removeItem(colorComboBox.getSelectedItem());
    }

    private void initCreatePlayerBtn() {
        createPlayerBtn = new JButton();
        createPlayerBtn.setText("Create Player");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(4, 0, 5, 0);
        this.add(createPlayerBtn, gridBagConstraints);
    }

    private void initName() {
        initNameInput();

        initNameLabel();
    }

    private void initNameLabel() {
        nameLabel = new JLabel();
        nameLabel.setText("Name");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        this.add(nameLabel, gridBagConstraints);
    }

    private void initNameInput() {
        nameIn = new JTextField();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        this.add(nameIn, gridBagConstraints);
    }

    private void initColor() {
        initColorLabel();

        initColorComboBox();
    }

    private void initColorComboBox() {
        colorComboBox = new JComboBox<>();
        colorComboBox.setModel(new DefaultComboBoxModel<>(Color.values()));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        this.add(colorComboBox, gridBagConstraints);
    }

    private void initColorLabel() {
        colorLabel = new JLabel();
        colorLabel.setText("Color");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new Insets(0, 4, 0, 4);
        this.add(colorLabel, gridBagConstraints);
    }

    private void initPlayerType() {
        initPlayerTypeLabel();

        initPlayerTypeCompboBox();
    }

    private void initPlayerTypeCompboBox() {
        playerTypeComboBox = new JComboBox<>();
        playerTypeComboBox.setModel(new DefaultComboBoxModel<>(PlayerType.values()));
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(4, 4, 4, 0);
        this.add(playerTypeComboBox, gridBagConstraints);
    }

    private void initPlayerTypeLabel() {
        playerTypeLabel = new JLabel();
        playerTypeLabel.setText("Player Type");
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        this.add(playerTypeLabel, gridBagConstraints);
    }

    // endregion

    public PlayerType getSelectedPlayerType() {
        return (PlayerType) this.playerTypeComboBox.getSelectedItem();
    }

    public Color getSelectedColor() {
        return (Color) this.colorComboBox.getSelectedItem();
    }

    public String getPlayerName() {
        String userName = this.nameIn.getText();
        if (userName == null || userName.equals("")) {
            userName = this.generateName();
            this.nameIn.setText(userName);
        }

        return userName;
    }

    private String generateName() {
        return new Faker().superhero().name();
    }

    public void reset() {
        colorComboBox.setModel(new DefaultComboBoxModel<>(Color.values()));

        nameIn.setText("");
        playerTypeComboBox.setSelectedItem(PlayerType.HUMAN);
    }

    public boolean canContinue() {
       return true;
    }
}
