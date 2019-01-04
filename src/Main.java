import Helpers.Tuple;
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

            int minDistance = 10;

            // Wanted frame width
            int screenW = 300;


            PreviewFrame preview = new PreviewFrame();
            preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            MazePreviewPanel mazePreviewPanel = new MazePreviewPanel(new Maze.Maze(height, width, minDistance, 2), new BasePlayer[]{new HumanPlayer(new Tuple<>(0, 0)), new ComputerPlayer(new Tuple<>(0, 0))}, true);
            mazePreviewPanel.setFocusable(true);
            mazePreviewPanel.requestFocusInWindow();

            preview.add(mazePreviewPanel);
            preview.setSize(screenW, screenW * (height / width));
            preview.setVisible(true);
        });
    }
}
