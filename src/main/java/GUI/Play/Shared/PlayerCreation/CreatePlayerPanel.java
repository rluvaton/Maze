package GUI.Play.Shared.PlayerCreation;

import GUI.Color;
import Helpers.CallbackFns;
import Helpers.Coordinate;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.ActionsKeys;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;
import player.HumanPlayer.HumanPlayer;
import player.PlayerType;

import javax.swing.*;
import java.awt.*;

import static Logger.LoggerManager.logger;

public class CreatePlayerPanel extends JPanel {

    private JLabel createPlayerHeader;
    private HumanPlayerSettingPanel humanPlayerSettingPanel;
    private BasePlayerCreationPanel basePlayerCreationPanel;

    private CallbackFns.ArgsVoidCallbackFunction<BasePlayer> onPlayerCreated;

    public CreatePlayerPanel(CallbackFns.ArgsVoidCallbackFunction<BasePlayer> onPlayerCreated) {
        ObjectAssertion.requireNonNull(onPlayerCreated, "`onPlayerCreated` can't be null");

        this.onPlayerCreated = onPlayerCreated;
    }

    public void initUIComponents() {

        this.setLayout(new GridBagLayout());

        initCreatePlayerHeader();
        initCreateBasePlayerSetting();
        initHumanPlayerSetting();

        setHumanPlayerSettingPanelVisibilityToDefault();
    }

    private void setHumanPlayerSettingPanelVisibilityToDefault() {
        this.onPlayerTypeSelected(this.basePlayerCreationPanel.getSelectedPlayerType());
    }

    private void initCreateBasePlayerSetting() {

        basePlayerCreationPanel = new BasePlayerCreationPanel(this::onPlayerTypeSelected, this::onCreatePlayerBtnClicked);
        basePlayerCreationPanel.initUIComponents();

        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        this.add(basePlayerCreationPanel, gridBagConstraints);
    }

    private void onPlayerTypeSelected(PlayerType playerType) {
        switch (playerType) {
            case HUMAN:
                setHumanPlayerSettingPanelVisibility(true);
                break;
            case COMPUTER:
                setHumanPlayerSettingPanelVisibility(false);
                break;
            default:
                return;
        }
    }

    private void setHumanPlayerSettingPanelVisibility(boolean isVisible) {
        humanPlayerSettingPanel.setVisible(isVisible);
    }

    private void onCreatePlayerBtnClicked() {
        onPlayerCreated.run(createNewPlayer());
        humanPlayerSettingPanel.reset();
    }

    public BasePlayer createNewPlayer() {
        PlayerType type = basePlayerCreationPanel.getSelectedPlayerType();

        BasePlayer player = null;

        switch (type) {
            case HUMAN:
                player = createHumanPlayer();
                break;
            case COMPUTER:
                player = createComputerPlayer();
                break;
            default:
                this.onPlayerCreationError();
                break;
        }
        return player;
    }

    private HumanPlayer createHumanPlayer() {
        Color color = basePlayerCreationPanel.getSelectedColor();
        String name = basePlayerCreationPanel.getPlayerName();

        int playerSpeed = humanPlayerSettingPanel.getPlayerSpeed();
        ActionsKeys actionsKeys = humanPlayerSettingPanel.getActionsKeys();

        return new HumanPlayer(new Coordinate(0, 0), name, color, playerSpeed, playerSpeed - 150, actionsKeys);
    }

    private ComputerPlayer createComputerPlayer() {
        Color color = basePlayerCreationPanel.getSelectedColor();
        String name = basePlayerCreationPanel.getPlayerName();

        return new ComputerPlayer(new Coordinate(0, 0), name, color);
    }

    private void onPlayerCreationError() {
        logger.error("Player Creation Error");
    }

    private void initHumanPlayerSetting() {
        humanPlayerSettingPanel = new HumanPlayerSettingPanel();
        humanPlayerSettingPanel.initUIComponents();

        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 15, 0, 0);

        this.add(humanPlayerSettingPanel, gridBagConstraints);
    }

    private void initCreatePlayerHeader() {
        GridBagConstraints gridBagConstraints;

        createPlayerHeader = new JLabel();
        createPlayerHeader.setHorizontalAlignment(SwingConstants.CENTER);
        createPlayerHeader.setText("Create Player");

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 19;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.BASELINE;

        this.add(createPlayerHeader, gridBagConstraints);
    }

    public void reset() {
        this.basePlayerCreationPanel.reset();
        this.humanPlayerSettingPanel.reset();

        setHumanPlayerSettingPanelVisibilityToDefault();
    }

    public boolean canContinue() {
        return this.basePlayerCreationPanel.canContinue() && (this.basePlayerCreationPanel.getSelectedPlayerType() != PlayerType.HUMAN || this.humanPlayerSettingPanel.canContinue());
    }

    public boolean isPlayerWithCurrentInputsValid() {
        return this.basePlayerCreationPanel.canContinue() && (this.basePlayerCreationPanel.getSelectedPlayerType() != PlayerType.HUMAN || this.humanPlayerSettingPanel.isPlayerWithCurrentInputsValid());
    }

}
