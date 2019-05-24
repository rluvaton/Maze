package GUI.Play.Shared.PlayerCreation;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Play.CustomGame.IPlayConfigStep;
import GUI.Play.CustomGame.PlayStep;
import Game.MazeGame;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.BasePlayer;

import javax.swing.*;
import java.awt.*;

public class PlayersCreationPanel extends JPanel implements IPlayConfigStep {

    private JSplitPane splitPaneContainer;
    private CreatePlayerPanel createPlayerSidePanel;
    private ExistingPlayerPanel existPlayersSidePanel;

    @Override
    public void init() {
        this.setLayout(new BorderLayout());
    }

    @Override
    public void initUIComponents() {

        initSplitPane();
    }

    private void initCreateExistPlayersSidePanel() {
        existPlayersSidePanel = new ExistingPlayerPanel();
        existPlayersSidePanel.initUIComponents();

        splitPaneContainer.setRightComponent(existPlayersSidePanel);
    }

    private void initCreatePlayerSidePanel() {
        createPlayerSidePanel = new CreatePlayerPanel(this::onPlayerCreated);
        createPlayerSidePanel.initUIComponents();

        splitPaneContainer.setTopComponent(createPlayerSidePanel);
    }

    private void onPlayerCreated(BasePlayer basePlayer) {
        this.existPlayersSidePanel.addPlayer(basePlayer);
    }

    private void initSplitPane() {
        splitPaneContainer = new JSplitPane();

        splitPaneContainer.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPaneContainer.setResizeWeight(0.3);
        splitPaneContainer.setOneTouchExpandable(true);

        initCreatePlayerSidePanel();
        initCreateExistPlayersSidePanel();

        this.add(splitPaneContainer, BorderLayout.CENTER);
    }

    @Override
    public void reset() {
        this.existPlayersSidePanel.reset();
        this.createPlayerSidePanel.reset();
    }

    @Override
    public boolean canContinue() {
        return this.existPlayersSidePanel.isTherePlayers() || this.createPlayerSidePanel.canContinue();
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.CREATE_PLAYERS;
    }

    @Override
    public MazeGame.Builder appendData(MazeGame.Builder builder) throws NotFinishedStepException {

        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        ObjectAssertion.requireNonNull(builder, "Builder can't be null");

        BasePlayer newPlayer = createPlayerSidePanel.createNewPlayer();

        if (this.createPlayerSidePanel.isPlayerWithCurrentInputsValid() &&
                !this.existPlayersSidePanel.isPlayerWithCurrentInputsExistAlready(newPlayer)) {
            this.existPlayersSidePanel.addPlayer(newPlayer);
        }

        return builder.setPlayers(existPlayersSidePanel.getPlayers());
    }
}
