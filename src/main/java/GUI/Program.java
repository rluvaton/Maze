package GUI;

import GUI.MazeGame.MazePanel;
import GUI.Play.CustomGame.CustomGameCreatorCard;
import GUI.Play.GameModeSelectionPanel;
import GUI.Play.StepsGame.StepGameCreatorCard;
import GUI.Stats.UsersStatPanel;
import GUI.Utils.GuiHelper;
import GUI.Welcome.WelcomePanel;
import Game.GameStep;
import Game.MazeGame;
import Helpers.Builder.BuilderException;
import Helpers.ThrowableAssertions.ObjectAssertion;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Optional;

import static Logger.LoggerManager.logger;

public class Program {
    private JPanel containerPanel;
    private JPanel cardsContainer;

    private JMenuBar menuBar;
    private JMenuItem backMenu;

    // region Cards

    private WelcomePanel welcomeCard;
    private UsersStatPanel statCard;
    private GameModeSelectionPanel gameConfigurationCard;
    private StepGameCreatorCard stepGameCreatorCard;
    private CustomGameCreatorCard customGameCreatorCard;

    // endregion

    private CardLayout cl;

    private CardName currentCardName = null;
    private CardName prevCardName = null;
    private static Frame frame;

    public Program() {
        ObjectAssertion.requireNonNull(frame, "Frame can't be null");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze");

        Program.frame = frame;
        Program program = new Program();
        program.createUIComponents();

        frame.setContentPane(program.containerPanel);
        setFrameIcon(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        // Set frame at the center of the screen
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }


    private static void setFrameIcon(JFrame frame) {
        ImageIcon img = new ImageIcon("C:\\Users\\rluva\\Programming\\FrontEnd\\Desktop\\Java\\Maze\\src\\main\\resources\\icons\\maze-game-icon-white.png");
        frame.setIconImage(img.getImage());
    }

    /**
     * @see Frame#pack()
     */
    public static void pack() {
        if(frame != null) {
            frame.pack();
        }
    }

    private void onGameUnexpectedlyFinished(Throwable throwable) {
        logger.error(throwable);
    }

    private void createUIComponents() {
        initContainerPanel();

        createAndAttachWelcomeCard();
        createAndAttachStatsCard();
        createAndAttachGameConfiguratorCard();

        cl = (CardLayout) (this.cardsContainer.getLayout());

        showCard(CardName.WELCOME);

    }

    private void initContainerPanel() {
        containerPanel = new JPanel();

        initMenuBar();

        cardsContainer = new JPanel();

        cardsContainer.setLayout(new PageViewer());

        cardsContainer.setMinimumSize(new Dimension(25, 25));

        containerPanel.setLayout(new BorderLayout());

        containerPanel.add(menuBar, BorderLayout.PAGE_START);
        containerPanel.add(cardsContainer, BorderLayout.CENTER);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        backMenu = new JMenuItem();
        backMenu.add(createBackBtn());
        backMenu.setEnabled(false);

        menuBar.add(backMenu);
    }

    private JButton createBackBtn() {
        JButton backBtn = new JButton();

        backBtn.setText("Back");

        backBtn.addActionListener(this::onBackBtnClicked);

        return backBtn;
    }

    private void onBackBtnClicked(ActionEvent e) {
        Optional<JPanel> currentCard = this.getCurrentCard();

        if (currentCard.isPresent() && currentCard.get() instanceof WindowCard) {
            try {
                if (!((WindowCard) currentCard.get()).back()) {
                    this.showCard(CardName.GAME_CREATOR);
                    this.currentCardName = CardName.GAME_CREATOR;
                }
            } catch (Exception ex) {
                this.showCard(CardName.WELCOME);

                this.currentCardName = CardName.WELCOME;
                setEnabledBack(false);
            }

            return;
        }

        if (currentCard.isPresent() && currentCard.get() instanceof MazePanel) {
            ((MazePanel) currentCard.get()).onFinishGame();
        }

        this.showCard(CardName.WELCOME);

        this.currentCardName = CardName.WELCOME;
        setEnabledBack(false);
    }

    private Optional<JPanel> getCurrentCard() {
        return GuiHelper.getCurrentCard(cardsContainer).map(component -> (JPanel)component);
    }

    private void createAndAttachWelcomeCard() {
        welcomeCard = new WelcomePanel(this::generatedClicked, this::playClicked, this::statsClicked);
        welcomeCard.init();

        addCard(welcomeCard, CardName.WELCOME);
        welcomeCard.initComponents();
    }


    private void createAndAttachPlayCard() {
        customGameCreatorCard = new CustomGameCreatorCard(this::onFinishMazeCreation);
        customGameCreatorCard.init();

        addCard(customGameCreatorCard, CardName.PLAY);
        customGameCreatorCard.initComponents();
    }

    private void createAndAttachGameConfiguratorCard() {

        gameConfigurationCard = new GameModeSelectionPanel(gameModes -> {
            ObjectAssertion.requireNonNull(gameModes, "Game Mode can't be null");

            switch (gameModes) {
                case STEPS:
                    onSelectedStepGameMode();
                    break;
                case CUSTOM:
                    onSelectedCustomGameMode();
                    break;
            }
        });

        addCard(gameConfigurationCard, CardName.GAME_CREATOR);
        gameConfigurationCard.initUIComponents();
    }

    private void onSelectedStepGameMode() {
        StepGameCreatorCard stepGameCreatorCard = new StepGameCreatorCard(this::onFinishMazeBuilding);
        stepGameCreatorCard.init();

        addCard(stepGameCreatorCard, CardName.STEPS_GAME_CREATOR);
        stepGameCreatorCard.initComponents();

        showCard(CardName.STEPS_GAME_CREATOR);
    }

    private void onSelectedCustomGameMode() {
        customGameCreatorCard = new CustomGameCreatorCard(this::onFinishMazeCreation);
        customGameCreatorCard.init();

        addCard(customGameCreatorCard, CardName.CUSTOM_GAME_CREATOR);
        customGameCreatorCard.initComponents();

        showCard(CardName.CUSTOM_GAME_CREATOR);
    }

    private void generatedClicked() {
        System.out.println("Not Supported Yet");
        showCard(CardName.CUSTOM_GAME_CREATOR);
    }

    private void playClicked() {
        System.out.println("Not Supported Yet");
        showCard(CardName.GAME_CREATOR);
    }

    private void statsClicked() {
        showCard(CardName.STATS);
    }

    private void createAndAttachStatsCard() {
        if (statCard == null) {
            statCard = new UsersStatPanel();
        }

        statCard.init();

        addCard(statCard, CardName.STATS);
        statCard.initComponents();
    }

    private void showCard(CardName cardName) {
        prevCardName = currentCardName;
        currentCardName = cardName;

        cl.show(this.cardsContainer, cardName.getValue());

        setEnabledBack(currentCardName != CardName.WELCOME);

//        Dimension size = this.cl.preferredLayoutSize(this.cardsContainer);
//
//        this.cardsContainer.setSize(size);
//        this.cardsContainer.setMinimumSize(size);
        frame.revalidate();
        frame.pack();
        frame.repaint();
    }

    private void addCard(JPanel panel, CardName cardName) {
        cardsContainer.add(panel, cardName.getValue());
    }

    private void onFinishMazeCreation(MazePanel mazePanel) {

        addCard(mazePanel, CardName.GAME);
        showCard(CardName.GAME);

        setEnabledBack(false);

        Dimension mazePanelSize = mazePanel.calculateMinimumSizeBasedOnMaze();
        setCardContainerAllSizes(mazePanelSize, mazePanel);
        setCardContainerAllSizes(mazePanelSize, this.cardsContainer);

        // Hack for sizing the container panel at better size
        this.containerPanel.setPreferredSize(this.containerPanel.getPreferredSize());

        this.frame.pack();


        mazePanel.initGame();

        mazePanel.setFocusable(true);
        mazePanel.requestFocusInWindow();

        mazePanel.startGame();
    }

    private void setCardContainerAllSizes(Dimension size, JPanel panel) {
        panel.setMinimumSize(size);
        panel.setPreferredSize(size);
        panel.setSize(size);
    }

    private void setEnabledBack(boolean enabled) {
        this.backMenu.setEnabled(enabled);
    }

    private void onFinishMazeBuilding(MazeGame.Builder mazeGameBuilder) {
        MazeGame game;

        try {
            game = mazeGameBuilder.clone().build();
        } catch (BuilderException e) {
            e.printStackTrace();
            return;
        }

        MazePanel mazePanel = new MazePanel(game);

        onFinishMazeCreation(mazePanel);

        mazePanel.getOnFinishGameObs().subscribe(
                player -> onGameFinished(mazeGameBuilder, player),
                this::onGameUnexpectedlyFinished
        );
    }

    private void onGameFinished(MazeGame.Builder mazeGameBuilder, BasePlayer player) {
        if (isPlayerNotFinishedTheGame(player)) {
            rerunTheSameStep(mazeGameBuilder);
            return;
        }

        switch (mazeGameBuilder.getStep().getName()) {
            case VERY_EASY:
                moveToNextStep(mazeGameBuilder, GameStep.EASY);
                break;
            case EASY:
                moveToNextStep(mazeGameBuilder, GameStep.MEDIUM);
                break;
            case MEDIUM:
                moveToNextStep(mazeGameBuilder, GameStep.HARD);
                break;
            case HARD:
                moveToNextStep(mazeGameBuilder, GameStep.VERY_HARD);
                break;
            case VERY_HARD:
                logger.verbose("All Steps finished!!");
                break;
        }
    }

    private void rerunTheSameStep(MazeGame.Builder mazeGameBuilder) {
        this.onFinishMazeBuilding(mazeGameBuilder.clone());
    }

    private boolean isPlayerNotFinishedTheGame(BasePlayer player) {
        return player instanceof ComputerPlayer;
    }

    private void moveToNextStep(MazeGame.Builder mazeGameBuilder, GameStep.BuiltinStep easy) {
        this.onFinishMazeBuilding(mazeGameBuilder.clone().setStep(easy));
    }
}
