import Helpers.Coordinate;
import Maze.Maze;
import Maze.MazeBuilder.RectangleMaze;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import UI.MazePreviewPanel;
import UI.PreviewFrame;
import player.BasePlayer;
import player.ComputerPlayer;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int height = 20;
            int width = 20;

            int minDistance = 3;

            // Wanted frame width
            int screenW = 300;


            PreviewFrame preview = new PreviewFrame();
            preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Maze maze;

//            maze = new Maze(height, width, minDistance, 2, 1);
            maze = new MazeGenerator(new RectangleMaze(), new BFSSolverAdapter())
                    .generateMaze(height, width, minDistance, 2, 2)
                    .generateRandomCandies((int) (height * width * 0.1))
                    .create();

            MazePreviewPanel mazePreviewPanel = new MazePreviewPanel(maze, new BasePlayer[]{new HumanPlayer(new Coordinate(0, 0)), new ComputerPlayer(new Coordinate(0, 0))}, true);
            mazePreviewPanel.setFocusable(true);
            mazePreviewPanel.requestFocusInWindow();

            preview.add(mazePreviewPanel);
            preview.setSize(screenW, screenW * (height / width));
            preview.setVisible(true);
        });
    }
}
