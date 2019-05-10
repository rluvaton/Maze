package GUI;

import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import GUI.MazePreviewPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import player.BasePlayer;
import player.ComputerPlayer;
import player.DirectionKeys;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class GameWindow {
    private JPanel wrapper;
    private JPanel usersMetadataPanel;
    private JPanel mazePanel;
    private MazePreviewPanel previewPanel;

    public GameWindow() {
    }

    public static void main(String[] args) {
        if (isInDebugMode(args)) {
            turnOnDebugEnv();
        }
        JFrame frame = new JFrame("GameWindow");

        GameWindow gameWindow = new GameWindow();
        gameWindow.createUIComponents();
        frame.setContentPane(gameWindow.wrapper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setFocusable(true);
        setFrameIcon(frame);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args, MazePreviewPanel previewPanel) {
        if (isInDebugMode(args)) {
            turnOnDebugEnv();
        }
        JFrame frame = new JFrame("GameWindow");

        GameWindow gameWindow = new GameWindow();
        gameWindow.createUIComponents(previewPanel);
        frame.setContentPane(gameWindow.wrapper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setFrameIcon(frame);

        frame.pack();
        frame.setVisible(true);
    }

    private static void setFrameIcon(JFrame frame) {
        ImageIcon img = new ImageIcon("C:\\Users\\rluva\\Programming\\FrontEnd\\Desktop\\Java\\Maze\\src\\main\\resources\\icons\\maze-game-icon-white.png");
        frame.setIconImage(img.getImage());
    }

    private static void turnOnDebugEnv() {
        DebuggerHelper.setDebugMode(true);
    }

    private static boolean isInDebugMode(String[] args) {
        return args.length > 0 && Objects.equals(args[0], "d");
    }

    private static double getTotalCandiesCountForMaze(int height, int width) {
        return height * width * 0.1;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        this.createWrapper();

        // TODO - clean this
        int height = 50;
        int width = 20;

        // TODO - add scroller to the game JPanel

        Dimension previewPanel = this.getWrapperDimensionForMazeDim(new Dimension(width, height));

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;


        int minDistance = 0;

        this.previewPanel = start(height, width, minDistance);


        Dimension wrapperSize = (Dimension) previewPanel.clone();
        int buttonHeight = 20;
        wrapperSize.setSize(previewPanel.width, previewPanel.height + buttonHeight);


        this.wrapper.setPreferredSize(wrapperSize);
//
//        c.gridx = 0;
//        c.gridy = 0;
//        this.wrapper.add(this.logSizeBtn, c);
//
//        c.gridx = 1;
//        c.gridy = 0;
//        c.weightx = 20;
//        this.wrapper.add(this.widthSpinner, c);
//
//        c.gridx = 2;
//        c.gridy = 0;
//        this.wrapper.add(this.heightSpinner, c);

        c.ipady = previewPanel.height;      // make this component tall
        c.weightx = previewPanel.width;
        c.weighty = previewPanel.height;
        c.gridwidth = previewPanel.width;
        c.gridx = 0;
        c.gridy = 1;
        this.wrapper.add(this.previewPanel, c);

        this.wrapper.updateUI();

        this.previewPanel.initGame();
    }

    private void createUIComponents(MazePreviewPanel previewPanel) {
        // TODO: place custom component creation code here

        this.createWrapper();

        // TODO - clean this
        int height = previewPanel.getMaze().getHeight();
        int width = previewPanel.getMaze().getWidth();

        // TODO - add scroller to the game JPanel

        Dimension previewPanelDim = this.getWrapperDimensionForMazeDim(new Dimension(width, height));

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;


        int minDistance = 0;

        this.previewPanel = previewPanel;


        Dimension wrapperSize = (Dimension) previewPanelDim.clone();
        int buttonHeight = 20;
        wrapperSize.setSize(previewPanelDim.width, previewPanelDim.height + buttonHeight);


        this.wrapper.setPreferredSize(wrapperSize);
//
//        c.gridx = 0;
//        c.gridy = 0;
//        this.wrapper.add(this.logSizeBtn, c);
//
//        c.gridx = 1;
//        c.gridy = 0;
//        c.weightx = 20;
//        this.wrapper.add(this.widthSpinner, c);
//
//        c.gridx = 2;
//        c.gridy = 0;
//        this.wrapper.add(this.heightSpinner, c);

        c.ipady = previewPanelDim.height;      // make this component tall
        c.weightx = previewPanelDim.width;
        c.weighty = previewPanelDim.height;
        c.gridwidth = previewPanelDim.width;
        c.gridx = 0;
        c.gridy = 1;
        this.wrapper.add(this.previewPanel, c);

        this.wrapper.updateUI();

        this.previewPanel.initGame();
    }


    private Dimension getWrapperDimensionForMazeDim(Dimension mazeDim) {
        int width = mazeDim.width * 25;
        int height = (int) (width * (((double) mazeDim.height) / ((double) mazeDim.width)));
        return new Dimension(width, height);
    }

    private void createWrapper() {
        this.wrapper = new JPanel(new GridBagLayout());

    }

    private MazePreviewPanel start(int height, int width, int minDistance) {
        Maze maze;

        try {
            maze = new MazeGenerator(new RectangleMazeBuilder(), new BFSSolverAdapter())
                    .generateMaze(height, width, minDistance, 2, 2)
                    .generateRandomCandies((int) getTotalCandiesCountForMaze(height, width), true)
                    .create();
        } catch (MazeBuilderException e) {
            e.printStackTrace();
            return null;
        }

        BasePlayer[] players = getGamePlayer();

        MazePreviewPanel mazePreviewPanel = new MazePreviewPanel(maze, players, false);
        mazePreviewPanel.setFocusable(true);
        mazePreviewPanel.requestFocusInWindow();

        return mazePreviewPanel;

    }

    private BasePlayer[] getGamePlayer() {
        return new BasePlayer[]{
                new HumanPlayer(new Coordinate(0, 0), "ArrowsPlayer"),
                new HumanPlayer(new Coordinate(0, 0), "WASDPlayer", DirectionKeys.DEFAULT_AS_WASD),
                new ComputerPlayer(new Coordinate(0, 0))
        };
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        wrapper.setLayout(new CardLayout(0, 0));
        wrapper.setMinimumSize(new Dimension(300, 300));
        wrapper.setOpaque(true);
        wrapper.setPreferredSize(new Dimension(300, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("fill:d:grow", "center:d:grow,top:4dlu:noGrow,center:d:grow(6.0)"));
        wrapper.add(panel1, "Card1");
        mazePanel = new JPanel();
        mazePanel.setLayout(new CardLayout(0, 0));
        CellConstraints cc = new CellConstraints();
        panel1.add(mazePanel, cc.xy(1, 3));
        usersMetadataPanel = new JPanel();
        usersMetadataPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(usersMetadataPanel, cc.xy(1, 1));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return wrapper;
    }

}
