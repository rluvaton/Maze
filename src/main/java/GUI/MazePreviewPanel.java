package GUI;

import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Helpers.Direction;
import Helpers.CallbackFns.NoArgsVoidCallbackFunction;
import Maze.Candy.Candy;
import Maze.Cell;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Maze;
import io.reactivex.Observable;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;
import player.HumanPlayer.HumanPlayer;
import player.MoveStatus;
import player.exceptions.PlayerNotRunning;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
     * From where to createRunningThread the maze X axis
     * Set to 20 so it createRunningThread with a little padding
     */
    private final int startX = 20;

    /**
     * From where to createRunningThread the maze Y axis
     * Set to 20 so it createRunningThread with a little padding
     */
    private final int startY = 20;

    /**
     * From where to createRunningThread the maze X axis
     * Set to 20 so it createRunningThread with a little padding
     */
    private final int cellVerMargin = 3;

    /**
     * From where to createRunningThread the maze Y axis
     * Set to 20 so it createRunningThread with a little padding
     */
    private final int cellHorMargin = 3;

    // region Constructors

    /**
     * Maze Preview Panel Base Constructor
     */
    public MazePreviewPanel() {
        init();
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param maze    Maze to build
     * @param players Players of the maze
     */
    public MazePreviewPanel(Maze maze, BasePlayer[] players) {
        this(maze, players, true);

        init();
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param maze        Maze to build
     * @param players     Players of the maze
     * @param atEntrances Set the players location at entrances
     */
    public MazePreviewPanel(Maze maze, BasePlayer[] players, boolean atEntrances) {
        this.maze = maze;
        this.players = players;

        if (!atEntrances) {
            setPlayerLocationAtEntrances(maze);
        }

        init();
    }

    private void setPlayerLocationAtEntrances(Maze maze) {
        List<Coordinate> locations = maze.getEntrances().stream().map(eLocation ->
                eLocation.getLocation()).collect(Collectors.toList());

        if (locations.size() == 0) {
            return;
        }

        int defaultIndex = 0;

        Coordinate defaultEntrance = locations.get(defaultIndex);
        locations.remove(defaultIndex);

        Coordinate playerLoc;

        for (BasePlayer player : this.players) {
            playerLoc = defaultEntrance;

            if (!locations.isEmpty()) {
                playerLoc = locations.get(0);
                locations.remove(0);
            }
            player.setLocation(playerLoc);
        }
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param cells   Cells of the maze
     * @param players Players of the maze
     */
    public MazePreviewPanel(Cell[][] cells, BasePlayer[] players) {
        this.maze = new Maze(cells);
        this.players = players;

        init();
    }

    private void init() {
        if (DebuggerHelper.isInDebugMode()) {
            this.initDebugging();
        }

        this.setFocusable(true);
    }

    // endregion

    private void initDebugging() {
        this.addKeyListener(DebuggerHelper.getInstance());
    }

    /**
     * Init Game
     *
     * @description Start listening for players movements
     */
    public void initGame() {
        setBackground(Color.WHITE);

        ArrayList<NoArgsVoidCallbackFunction> startPlayersCallbacks = new ArrayList<>();

        for (BasePlayer player : this.players) {

            // Create entrance
            ELocation entrance = this.maze.getRandomEntrance();

            // Set default location to 0,0
            player.setLocation(entrance != null ? entrance.getLocation() : new Coordinate(0, 0));

            // Move player when observable fire
            player.getPlayerMoveObs()
                    .subscribe(direction -> {
                        MoveStatus res = this.movePlayer(player, direction);
                        switch (res) {
                            case Valid:
                                System.out.println("Time is: " + player.getTime() + " | Points is: " + player.getPoints());
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

            // TODO - MOVE TO SINGLE FUNCTION CALL THAT WILL START START THE PLAYERS
            // Set the key listener to the player if is a human player
            if (player instanceof HumanPlayer) {
//                this.addKeyListener((HumanPlayer) player);
                startPlayersCallbacks.add(() -> {
                    addKeyListener((HumanPlayer) player);
                    Thread playerThread = ((HumanPlayer) player).start();

                    playerThread.start();
                });
            } else if (player instanceof ComputerPlayer) {
                startPlayersCallbacks.add(() -> {
                    Thread playerThread = ((ComputerPlayer) player).createRunningThread(this.maze, getExitForComputerPlayer((ComputerPlayer) player));

                    if (playerThread == null) {
                        System.out.println("Player " + player.getName() + " can't start running");
                        return;
                    }

                    playerThread.start();
                });
            }
        }

        // Start the players
        startPlayersCallbacks.forEach(NoArgsVoidCallbackFunction::run);

        this.maze.getCandies()
                .stream()
                .filter(candyLoc -> candyLoc.item1.getTimeToLive() > 0)
                .forEach(candyLoc -> Observable.timer(candyLoc.item1.getTimeToLive(), TimeUnit.MILLISECONDS)
                        .subscribe(finished -> this.maze.getCell(candyLoc.item2)
                                .removeCandy(candyLoc.item1)));
    }

    private Coordinate getExitForComputerPlayer(ComputerPlayer player) {
        return this.maze.getExits().stream().filter(eLocation -> !eLocation.getLocation().equals(player.getLocation())).findFirst().get().getLocation();
    }

    /**
     * Player Finished
     *
     * @param player The player that finished
     */
    private void playerFinished(BasePlayer player) {
        // Remove key listener if the player is human player
        player.onPlayerFinished();

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
                this.paintCell(g, x, y, verSpace, horSpace, horSpace, verSpace, maze.getCell(i, j), i, j);
                x += horSpace;
            }
            y += verSpace;
            x = startX;
        }

        // TODO - Add arrow directed to maze at the entrance points and another arrow directed outside in the exit points

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
     * @param row   Cell row at maze
     * @param col   Cell column at maze
     */
    private void paintCell(Graphics g, int x, int y, int len, int space, Cell cell, int row, int col) {
        this.paintCell(g, x, y, len, len, space, space, cell, row, col);
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
     * @param row      Cell row at maze
     * @param col      Cell column at maze
     */
    private void paintCell(Graphics g, int x, int y, int verLen, int horLen, int verSpace, int horSpace, Cell cell, int row, int col) {
        if (cell == null) {
            cell = new Cell(row, col);
        }

        // Top Wall
        if (!cell.haveCellOrELocationAtDirection(Direction.UP)) {
            g.drawLine(x, y, x + horLen, y);
        }

        // Bottom Wall
        if (!cell.haveCellOrELocationAtDirection(Direction.DOWN)) {
            g.drawLine(x, y + horSpace, x + horLen, y + horSpace);
        }

        // Left Wall
        if (!cell.haveCellOrELocationAtDirection(Direction.LEFT)) {
            g.drawLine(x, y, x, y + verLen);
        }

        // Right Wall
        if (!cell.haveCellOrELocationAtDirection(Direction.RIGHT)) {
            g.drawLine(x + verSpace, y, x + verSpace, y + verLen);
        }

        Color before = g.getColor();


        ArrayList<Candy> cellCandies = (ArrayList<Candy>) cell.getCandies().clone();

        if (!cellCandies.isEmpty()) {
            for (Candy candy : cellCandies) {
                if (candy == null) {
                    continue;
                }

                g.setColor(Color.decode(candy.getColor()));
                g.drawOval(x + horLen / 2, y + verLen / 2, horLen / 5, verLen / 5);
            }
        }

        g.setColor(before);
    }

    /**
     * Display the players in the maze
     *
     * @param g Graphics of the panel
     */
    private void showPlayers(Graphics g) {

        Color before = g.getColor();
        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        for (BasePlayer player : this.players) {
            if (player.getColor() != null) {
                g.setColor(player.getColor().getColor());
            }
            Coordinate coordinates = this.calculateLocation(player.getLocation());

            int horSpace = fullW / this.maze.getWidth();
            int verSpace = fullH / this.maze.getHeight();

            g.drawRect(coordinates.getRow() + this.cellHorMargin,
                    coordinates.getColumn() + this.cellVerMargin,
                    horSpace - this.cellHorMargin,
                    verSpace - this.cellVerMargin);

            g.fillRect(coordinates.getRow() + this.cellHorMargin,
                    coordinates.getColumn() + this.cellVerMargin,
                    horSpace - this.cellHorMargin,
                    verSpace - this.cellVerMargin);
            repaint();
        }

        g.setColor(before);
    }

    // endregion

    /**
     * Calculate location in the panel
     *
     * @param location Location in the matrix
     * @return The x, y coordinates of the location in the panel
     */
    private Coordinate calculateLocation(Coordinate location) {

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        int horSpace = fullW / this.maze.getWidth();
        int verSpace = fullH / this.maze.getHeight();

        return new Coordinate(startX + horSpace * location.getColumn(), startY + verSpace * location.getRow());
    }

    /**
     * Move Player Handler
     *
     * @param player    Player that wanted to move
     * @param direction The direction of the move
     * @return Returns the status of the move ${@link MoveStatus}
     * @see #changePlayerLocation(BasePlayer, Coordinate, boolean) Change Player Location - (i.e teleportation)
     */
    private MoveStatus movePlayer(BasePlayer player, Direction direction) {
        Coordinate loc = player.getLocation();

        Cell cell;
        Coordinate teleportLocation;

        // Moved status
        MoveStatus moved = MoveStatus.NotValidMove;

        if (this.maze.checkIfValidMove(loc, direction) != null) {
            player.setLocation(direction);

            cell = this.maze.getCell(player.getLocation());

            if (cell != null) {
                // Add the time and points from the candy
                player.addTime(cell.collectTimeCandyStrengths());
                player.addPoints(cell.collectPointsCandyStrengths());

                teleportLocation = cell.collectLocationCandyPortal();

                if (teleportLocation != null) {
                    this.changePlayerLocation(player, teleportLocation, true);
                }

                System.out.println("Player " + player.getName() + " Moved");
                moved = MoveStatus.Valid;
            }
        } else if ((cell = this.maze.getCell(loc)) != null && (teleportLocation = cell.collectLocationCandyPortal()) != null) {
            this.changePlayerLocation(player, teleportLocation, true);

            System.out.println("Player " + player.getName() + " Moved");
            moved = MoveStatus.Valid;
        }

        // Don't use player.getLocation() because it will bring you the new location
        return this.maze.checkIfELocation(loc, direction, ELocationType.Exit) != null ? MoveStatus.Finished : moved;
    }

    /**
     * Change Player Location
     *
     * @param player       Player to move
     * @param location     location to move the player to
     * @param isTeleported Is the location changed due to teleportation?
     * @return Returns if the played moved
     * @see #movePlayer(BasePlayer, Direction) For Moving Player by direction of move
     */
    private boolean changePlayerLocation(BasePlayer player, Coordinate location, boolean isTeleported) {
        Coordinate loc = player.getLocation();

        if (!this.maze.isValidLocation(location)) {
            return false;
        }

        player.setLocation(location);

        Cell cell = this.maze.getCell(player.getLocation());

        // Add the time and points from the candy
        player.addTime(cell.collectTimeCandyStrengths());
        player.addPoints(cell.collectPointsCandyStrengths());

        if (!isTeleported) {
            Coordinate teleportLocation = cell.collectLocationCandyPortal();

            if (teleportLocation != null) {
                this.changePlayerLocation(player, teleportLocation, true);
            }
        }

        if (isTeleported) {
            System.out.println("Teleported");
            try {
                player.onPlayerTeleported();
            } catch (PlayerNotRunning playerNotRunning) {
                playerNotRunning.printStackTrace();
                // TODO - do something
            }
        } else {
            System.out.println("Changed location");
        }

        return true;
    }

    public Maze getMaze() {
        return maze;
    }
}
