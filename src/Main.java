import UI.MazePreviewPanel;
import UI.PreviewFrame;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                PreviewFrame preview = new PreviewFrame();
                preview.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                preview.add(new MazePreviewPanel(new Maze.Maze(10, 10)));
                preview.setSize(300,300);
                preview.setVisible(true);
            }
        });
    }
}
