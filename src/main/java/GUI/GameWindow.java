package GUI;

import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import player.BasePlayer;
import player.ComputerPlayer;
import player.DirectionKeys;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameWindow {
    private JPanel wrapper;
    private MazePreviewPanel previewPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("GameWindow");

        GameWindow gameWindow = new GameWindow();
        gameWindow.createUIComponents();
        frame.setContentPane(gameWindow.wrapper);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    private static boolean isInDebugMode(String[] args) {
        return args.length > 0 && Objects.equals(args[0], "d");
    }

    private static double getTotalCandiesCountForMaze(int height, int width) {
        return height * width * 0.1;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.previewPanel = start();

        this.wrapper.setLayout(new GridLayout(1, 1));

        // TODO - clean this
        int height = 20;
        int width = 20;

        // Wanted frame width
        int screenW = 300;

        this.wrapper.setSize(screenW, screenW * (height / width));

        this.wrapper.add(this.previewPanel);

        this.wrapper.updateUI();

        this.previewPanel.initGame();
    }

    private MazePreviewPanel start() {
        int height = 20;
        int width = 20;

        int minDistance = 3;

        PreviewFrame preview = new PreviewFrame();
        preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
