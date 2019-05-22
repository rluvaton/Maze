package GUI;

import GUI.MazeGame.MazePanel;
import GUI.Play.CustomGame.PlayCard;
import GUI.Play.GameModeSelectionPanel;
import GUI.Stats.UsersStatPanel;
import GUI.Welcome.WelcomePanel;
import Helpers.ThrowableAssertions.ObjectAssertion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Window {
    private JPanel containerPanel;
    private JPanel cardsContainer;

    private JMenuBar menuBar;
    private JMenuItem backMenu;

    // region Cards

    private WelcomePanel welcomeCard;
    private UsersStatPanel statCard;
    private GameModeSelectionPanel gameConfigurationCard;
    private PlayCard customGameCreatorCard;

    // endregion

    private CardLayout cl;

    private CardName currentCardName = null;
    private CardName prevCardName = null;
    private Frame frame;

    public Window(Frame frame) {
        ObjectAssertion.requireNonNull(frame, "Frame can't be null");

        this.frame = frame;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze");

        Window window = new Window(frame);
        window.createUIComponents();

        frame.setContentPane(window.containerPanel);
        setFrameIcon(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();

        // Set frame at the center of the screen
        frame.setLocationRelativeTo(null);
//        frame.setResizable(false);

        frame.setVisible(true);
    }


    private static void setFrameIcon(JFrame frame) {
        ImageIcon img = new ImageIcon("C:\\Users\\rluva\\Programming\\FrontEnd\\Desktop\\Java\\Maze\\src\\main\\resources\\icons\\maze-game-icon-white.png");
        frame.setIconImage(img.getImage());
    }

    private void createUIComponents() {
        initContainerPanel();
//        initCardContainer();

        createAndAttachWelcomeCard();
        createAndAttachStatsCard();
//        createAndAttachPlayCard();
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
        if (this.currentCardName == CardName.CUSTOM_GAME_CREATOR) {
            if (!this.customGameCreatorCard.back()) {
                this.showCard(CardName.GAME_CREATOR);

                this.currentCardName = CardName.GAME_CREATOR;
            }

            return;
        }

        this.showCard(CardName.WELCOME);

        this.currentCardName = CardName.WELCOME;
        setEnabledBack(false);
    }

    private void createAndAttachWelcomeCard() {
        welcomeCard = new WelcomePanel(this::generatedClicked, this::playClicked, this::statsClicked);
        welcomeCard.init();

        addCard(welcomeCard, CardName.WELCOME);
        welcomeCard.initComponents();
    }


    private void createAndAttachPlayCard() {
        customGameCreatorCard = new PlayCard(this::onFinishMazeCreation);
        customGameCreatorCard.init();

        addCard(customGameCreatorCard, CardName.PLAY);
        customGameCreatorCard.initComponents();
    }

    private void createAndAttachGameConfiguratorCard() {

        gameConfigurationCard = new GameModeSelectionPanel(gameModes -> {
            ObjectAssertion.requireNonNull(gameModes, "Game Mode can't be null");

            switch (gameModes) {
                case STEPS:

                    break;
                case CUSTOM:
                    onSelectedCustomGameMode();
                    break;
            }
        });

        addCard(gameConfigurationCard, CardName.GAME_CREATOR);
        gameConfigurationCard.initUIComponents();
    }

    private void onSelectedCustomGameMode() {
        customGameCreatorCard = new PlayCard(this::onFinishMazeCreation);
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

        Dimension size = this.cl.preferredLayoutSize(this.cardsContainer);

        this.cardsContainer.setSize(size);
        this.cardsContainer.setMinimumSize(size);
    }

    private void addCard(JPanel panel, CardName cardName) {
        cardsContainer.add(panel, cardName.getValue());
    }

    private void onFinishMazeCreation(MazePanel mazePanel) {

        addCard(mazePanel, CardName.GAME);


        showCard(CardName.GAME);

        setEnabledBack(false);

        Dimension mazePanelSize = mazePanel.getPreferredSize();
        setCardContainerAllSizes(mazePanelSize, mazePanel);

        setCardContainerAllSizes(mazePanelSize, this.cardsContainer);

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
}
