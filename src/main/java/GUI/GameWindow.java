package GUI;

import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import UI.MazePreviewPanel;
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
    private JButton logSizeBtn;
    private JSpinner heightSpinner;
    private JSpinner widthSpinner;
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
        this.createGetSizeBtn();

        // TODO - clean this
        int height = 50;
        int width = 20;

        // TODO - add scroller to the game JPanel

        Dimension previewPanel = this.getWrapperDimensionForMazeDim(new Dimension(width, height));
        this.createWidthSpinner(previewPanel.width);
        this.createHeightSpinner(previewPanel.height);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;


        int minDistance = 0;

        this.previewPanel = start(height, width, minDistance);


        Dimension wrapperSize = (Dimension) previewPanel.clone();
        int buttonHeight = 20;
        wrapperSize.setSize(previewPanel.width, previewPanel.height + buttonHeight);


        this.wrapper.setPreferredSize(wrapperSize);

        c.gridx = 0;
        c.gridy = 0;
        this.wrapper.add(this.logSizeBtn, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 20;
        this.wrapper.add(this.widthSpinner, c);

        c.gridx = 2;
        c.gridy = 0;
        this.wrapper.add(this.heightSpinner, c);

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

    private void createHeightSpinner(int value) {
        heightSpinner = new JSpinner();
        heightSpinner.setToolTipText("Height");

        this.heightSpinner.setValue(value);

        heightSpinner.addChangeListener(e -> previewPanel.setSize(previewPanel.getWidth(), (Integer) heightSpinner.getValue()));
    }

    private void createWidthSpinner(int value) {
        widthSpinner = new JSpinner();
        widthSpinner.setToolTipText("Width");

        this.widthSpinner.setValue(value);

        widthSpinner.addChangeListener(e -> previewPanel.setSize((Integer) widthSpinner.getValue(), previewPanel.getHeight()));
    }

    private void createGetSizeBtn() {
        logSizeBtn = new JButton("Get Size");
        logSizeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(" ------------------------------ ");
                System.out.println("[Wrapper][Size]      " + wrapper.getSize());
                System.out.println("[PreviewPanel][Size] " + previewPanel.getSize());
            }
        });
    }

    private Dimension getWrapperDimensionForMazeDim(Dimension mazeDim) {
        int width = mazeDim.width * 25;
        int height = (int)(width * (((double)mazeDim.height) / ((double)mazeDim.width)));
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
}
