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
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        if(isInDebugMode(args)) {
            DebuggerHelper.setDebugMode(true);
        }

        EventQueue.invokeLater(() -> {
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

            MazePreviewPanel mazePreviewPanel = new MazePreviewPanel(maze, new BasePlayer[]{new HumanPlayer(new Coordinate(0, 0)), new ComputerPlayer(new Coordinate(0, 0))}, true);
            mazePreviewPanel.setFocusable(true);
            mazePreviewPanel.requestFocusInWindow();

            preview.add(mazePreviewPanel);
            preview.setSize(screenW, screenW * (height / width));
            preview.setVisible(true);
        });
    }

    private static boolean isInDebugMode(String[] args) {
        return args.length > 0 && Objects.equals(args[0], "d");
    }

    private static double getTotalCandiesCountForMaze(int height, int width) {
        return height * width * 0.1;
    }
}
