import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import UI.MazePreviewPanel;
import UI.PreviewFrame;
import player.BasePlayer;
import player.ComputerPlayer;
import player.DirectionKeys;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class Game {

    public static void main(String[] args) {
        if (isInDebugMode(args)) {
            DebuggerHelper.setDebugMode(true);
        }

        EventQueue.invokeLater(Game::run);
    }

    private static boolean isInDebugMode(String[] args) {
        return args.length > 0 && Objects.equals(args[0], "d");
    }

    private static double getTotalCandiesCountForMaze(int height, int width) {
        return height * width * 0.1;
    }

    private static void run() {
        Game game = new Game();
        game.start();

    }
    MazePreviewPanel mazePreviewPanel;

    private void start() {
        int height = 20;
        int width = 20;

        int minDistance = 3;

        // Wanted frame width
        int screenW = 300;


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
            return;
        }

        BasePlayer[] players = getGamePlayer();

        mazePreviewPanel = new MazePreviewPanel(maze, players, false);
        mazePreviewPanel.setFocusable(true);
        mazePreviewPanel.requestFocusInWindow();

        preview.add(mazePreviewPanel);
        preview.setSize(screenW, screenW * (height / width));
        preview.setVisible(true);
    }

    private BasePlayer[] getGamePlayer() {
        return new BasePlayer[]{
                    new HumanPlayer(new Coordinate(0, 0), "ArrowsPlayer"),
                    new HumanPlayer(new Coordinate(0, 0), "WASDPlayer", DirectionKeys.DEFAULT_AS_WASD),
                    new ComputerPlayer(new Coordinate(0, 0))
            };
    }
}
