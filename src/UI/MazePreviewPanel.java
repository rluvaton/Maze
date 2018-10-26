package UI;

import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;

public class MazePreviewPanel extends JPanel
{
    private Maze maze;

    public MazePreviewPanel() {

    }

    public MazePreviewPanel(Maze maze) {
        this.maze = maze;
    }

    public MazePreviewPanel(Cell[][] cells) {
        this.maze = new Maze(cells);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintMaze(g, Color.BLUE);
    }

    private void paintMaze(Graphics g, Color color) {
        g.setColor(color);

        // Set to 20 so it start with a little padding
        int startX = 20;
        int startY = 20;

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        int horSpace = fullH / this.maze.getHeight();
        int verSpace = fullW / this.maze.getWidth();

        int x = startX;
        int y = startY;

        for(int i = 0, h =  this.maze.getHeight(), w = this.maze.getWidth(); i < h; i++) {
            for(int j = 0; j < w; j++) {
                this.paintCell(g, x, y, verSpace, horSpace, horSpace, verSpace, maze.getCellAt(i, j));
                x += horSpace;
            }
            y += verSpace;
            x = startX;
        }
    }


    /**
     * Paint Square Cell
     *
     * @param g     Graphic
     * @param x     Start painting at X point
     * @param y     Start painting at Y point
     * @param len   Length of each line
     * @param space Space between each lines (space between horizontal lines and space between vertical lines)
     * @param cell  Cell to pain, if null then it will paint all the walls
     */
    private void paintCell(Graphics g, int x, int y, int len, int space, Cell cell) {
        this.paintCell(g, x, y, len, len, space, space, cell);
    }

    /**
     * Paint Cell
     *
     * @param g        Graphic
     * @param x        Start painting at X point
     * @param y        Start painting at Y point
     * @param verLen   Length of vertical line
     * @param horLen   Length of horizontal line
     * @param verSpace Space between vertical lines
     * @param horSpace Space between horizontal lines
     * @param cell     Cell to pain, if null then it will paint all the walls
     */
    private void paintCell(Graphics g, int x, int y, int verLen, int horLen, int verSpace, int horSpace, Cell cell) {
        if (cell == null) {
            cell = new Cell();
        }

        // Top Wall
        if (cell.haveTopWall()) {
            g.drawLine(x, y, x + horLen, y);
        }

        // Bottom Wall
        if (cell.haveBottomWall()) {
            g.drawLine(x, y + horSpace, x + horLen, y + horSpace);
        }

        // Left Wall
        if (cell.haveLeftWall()) {
            g.drawLine(x, y, x, y + verLen);
        }

        // Right Wall
        if (cell.haveRightWall()) {
            g.drawLine(x + verSpace, y, x + verSpace, y + verLen);
        }
    }
}
