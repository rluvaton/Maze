package UI;

import Helpers.Direction;
import Helpers.Tuple;
import Maze.Cell;
import Maze.Maze;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.MazeSolver.DFS.DFSCell;
import player.BasePlayer;
import player.HumanPlayer;
import player.MoveStatus;

import javax.swing.*;
import java.awt.*;

public class MazePreviewPanel extends JPanel {

    /**
     * Maze
     */
    private Maze maze;

    /**
     * Maze Players
     */
    private BasePlayer[] players;

    /**
     * Background Color
     */
    private Color background = Color.WHITE;

    /**
     * Maze Color, The colors of the maze's borders
     */
    private Color mazeColor = Color.BLUE;

    /**
     * From where to start the maze X axis
     * Set to 20 so it start with a little padding
     */
    private int startX = 20;

    /**
     * From where to start the maze Y axis
     * Set to 20 so it start with a little padding
     */
    private int startY = 20;

    // region Constructors

    /**
     * Maze Preview Panel Base Constructor
     */
    public MazePreviewPanel() {
        initGame();
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param maze    Maze to build
     * @param players Players of the maze
     */
    public MazePreviewPanel(Maze maze, BasePlayer[] players) {
        this.maze = maze;
        this.players = players;
        initGame();
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param cells   Cells of the maze
     * @param players Players of the maze
     */
    public MazePreviewPanel(DFSCell[][] cells, BasePlayer[] players) {
        this.maze = new Maze(cells);
        this.players = players;
        initGame();
    }

    // endregion

    /**
     * Init Game
     *
     * @description Start listening for players movements
     */
    private void initGame() {
        setBackground(Color.WHITE);

        for (BasePlayer player : this.players) {

            // Create entrance
            ELocation entrance = this.maze.getRandomEntrance();

            // Set default location to 0,0
            player.setLocation(entrance != null ? entrance.getLocation() : new Tuple<>(0, 0));

            // Move player when observable fire
            player.getPlayerMoveObs().subscribe(direction -> {
                MoveStatus res = this.movePlayer(player, direction);
                switch (res) {
                    case Valid:
                        break;
                    case NotValidMove:
                        System.out.println("Not Valid Move");
                        break;
                    case Finished:
                        System.out.println("User Finished!");
                        this.playerFinished(player);
                        break;
                    default:
                        System.out.println("Unknown move status " + res);
                        break;
                }
            });

            // Set the key listener to the player if is a human player
            if (player instanceof HumanPlayer) {
                this.addKeyListener((HumanPlayer) player);
            }
        }
    }

    /**
     * Player Finished
     *
     * @param player The player that finished
     */
    private void playerFinished(BasePlayer player) {
        // Remove key listener if the player is human player
        if (player instanceof HumanPlayer) {
            this.removeKeyListener((HumanPlayer) player);
        }
    }

    // region GUI Painting

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintMaze(g);
        this.showPlayers(g);
    }

    /**
     * Paint Maze
     *
     * @param g Graphics
     */
    private void paintMaze(Graphics g) {
        g.setColor(this.mazeColor);

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

    /**
     * Display the players in the maze
     *
     * @param g Graphics of the panel
     */
    private void showPlayers(Graphics g) {

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        for (BasePlayer player : this.players) {

            Tuple<Integer, Integer> coordinates = this.calculateLocation(player.getLocation());


            int horSpace = fullW / this.maze.getWidth();
            int verSpace = fullH / this.maze.getHeight();

            g.draw3DRect(coordinates.item1, coordinates.item2, horSpace, verSpace, true);
            g.fill3DRect(coordinates.item1, coordinates.item2, horSpace, verSpace, true);
            repaint();
        }
    }

    // endregion

    /**
     * Calculate location in the panel
     *
     * @param location Location in the matrix
     * @return The x, y coordinates of the location in the panel
     */
    private Tuple<Integer, Integer> calculateLocation(Tuple<Integer, Integer> location) {

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        int horSpace = fullW / this.maze.getWidth();
        int verSpace = fullH / this.maze.getHeight();

        int x = startX;
        int y = startY;

        y += verSpace * location.item1;
        x += horSpace * location.item2;

        return new Tuple<>(x, y);
    }

    /**
     * Move Player Handler
     *
     * @param player    Player that wanted to move
     * @param direction The direction of the move
     * @return Returns the status of the move ${@link MoveStatus}
     */
    private MoveStatus movePlayer(BasePlayer player, Direction direction) {
        // TODO - IMPLEMENT THIS METHOD
        Tuple<Integer, Integer> loc = player.getLocation();

        // Moved status
        MoveStatus moved = MoveStatus.NotValidMove;

        if (this.maze.checkIfValidMove(loc, direction) != null) {
            player.setLocation(direction);

            System.out.println("Move");
            moved = MoveStatus.Valid;
        }

        // Don't use player.getLocation because it will bring you the new location
        return this.maze.checkIfELocation(loc, direction, ELocationType.Exit) != null ? MoveStatus.Finished : moved;
    }
}
