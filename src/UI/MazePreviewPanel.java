package UI;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;
import Maze.MazeSolver.DFS.DFSCell;
import player.BasePlayer;
import player.HumanPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.stream.Stream;

public class MazePreviewPanel extends JPanel
{
    private Maze maze;
    private BasePlayer[] players;

    public MazePreviewPanel() {
        initGame();
    }

    public MazePreviewPanel(Maze maze, BasePlayer[] players) {
        this.maze = maze;
        this.players = players;
        initGame();
    }

    public MazePreviewPanel(DFSCell[][] cells, BasePlayer[] players) {
        this.maze = new Maze(cells);
        this.players = players;
        initGame();
    }

    private void initGame() {
        for (BasePlayer player : this.players) {
            if(player instanceof HumanPlayer) {
                this.addKeyListener((HumanPlayer)player);
            }
            player.getPlayerMoveObs()
                    .subscribe(direction -> {
                        this.movePlayer(player, direction);
                    });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintMaze(g, Color.BLUE);
        this.showPlayers(g);
    }

    private void paintMaze(Graphics g, Color color) {
        g.setColor(color);

        // Set to 20 so it start with a little padding
        int startX = 20;
        int startY = 20;

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        int horSpace = fullW / this.maze.getWidth();
        int verSpace = fullH / this.maze.getHeight();

        int x = startX;
        int y = startY;

        for (int i = 0, h = this.maze.getHeight(), w = this.maze.getWidth(); i < h; i++) {
            for (int j = 0; j < w; j++) {
                this.paintCell(g, x, y, verSpace, horSpace, horSpace, verSpace, maze.getCellAt(i, j));
                x += horSpace;
            }
            y += verSpace;
            x = startX;
        }

        // Draw the exists

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

        Color before = g.getColor();

        if (!cell.getCandies().isEmpty()) {
            cell.getCandies().forEach(candy -> {
                switch (candy.getType()) {
                    case Time:
                        g.setColor(Color.decode("#6761A8"));
                        break;
                    case Points:
                        g.setColor(Color.decode("#F26430"));
                        break;
                    case Location:
                        g.setColor(Color.decode("#009B72"));
                        break;
                    default:
                        return;
                }
                g.drawOval(x + horLen / 2, y + verLen / 2, horLen / 5, verLen / 5);
            });
        }

        g.setColor(before);
    }

    private Tuple<Integer, Integer> calculateLocation(Tuple<Integer, Integer> location) {

        int startX = 20;
        int startY = 20;

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        int horSpace = fullW / this.maze.getWidth();
        int verSpace = fullH / this.maze.getHeight();

        int x = startX;
        int y = startY;

        y += verSpace * location.item1;
        x += horSpace * location.item2;

//        for (int i = 0, h = this.maze.getHeight(), w = this.maze.getWidth(); i < h; i++) {
//            for (int j = 0; j < w; j++) {
//                this.paintCell(g, x, y, verSpace, horSpace, horSpace, verSpace, maze.getCellAt(i, j));
//                x += horSpace;
//            }
//            y += verSpace;
//            x = startX;
//        }

        return new Tuple<>(x, y);
    }

    private void showPlayers(Graphics g) {
        for (int i = 0; i < this.players.length; i++) {

            Tuple<Integer, Integer> coordinates = this.calculateLocation(this.players[i].getLocation());


            int startX = 20;
            int startY = 20;

            int fullW = getWidth() - startX * 2;
            int fullH = getHeight() - startY * 2;

            int horSpace = fullW / this.maze.getWidth();
            int verSpace = fullH / this.maze.getHeight();


            g.draw3DRect(coordinates.item1, coordinates.item2, horSpace, verSpace, true);
            g.fill3DRect(coordinates.item1, coordinates.item2, horSpace, verSpace, true);
            repaint();
        }
    }

    private void movePlayer(BasePlayer player, Direction direction) {
        // TODO - IMPLEMENT THIS METHOD
        System.out.println("Move");
        if(this.maze.getCell(player.getLocation()).haveCellAtDirection(direction)) {
            player.setLocation(direction);
        }
    }
}
