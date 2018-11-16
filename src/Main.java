import Helpers.Tuple;
import UI.MazePreviewPanel;
import UI.PreviewFrame;
import player.BasePlayer;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                int height = 20;
                int width = 20;

                // Wanted frame width
                int screenW = 300;



                PreviewFrame preview = new PreviewFrame();
                preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                MazePreviewPanel mazePreviewPanel = new MazePreviewPanel(new Maze.Maze(height, width), new BasePlayer[]{new HumanPlayer(new Tuple<>(0, 0))});
                mazePreviewPanel.setFocusable(true);
                mazePreviewPanel.requestFocusInWindow();

                preview.add(mazePreviewPanel);
                preview.setSize(screenW,screenW * (height / width));
                preview.setVisible(true);
            }
        });
    }
}
