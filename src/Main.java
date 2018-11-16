import UI.MazePreviewPanel;
import UI.PreviewFrame;

import javax.swing.*;
import java.awt.*;

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
                preview.add(new MazePreviewPanel(new Maze.Maze(height, width)));
                preview.setSize(screenW,screenW * (height / width));
                preview.setVisible(true);
            }
        });
    }
}
