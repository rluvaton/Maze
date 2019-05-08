import GUI.GameWindow;
import GUI.UsersStartsViewer;
import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import GUI.MazePreviewPanel;
import GUI.PreviewFrame;
import player.BasePlayer;
import player.ComputerPlayer;
import player.DirectionKeys;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Game {

    public static void main(String[] args) {
        GameWindow.main(args);

//        EventQueue.invokeLater(Game::run);
    }
}
