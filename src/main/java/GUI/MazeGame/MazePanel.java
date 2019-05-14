package GUI.MazeGame;

import Helpers.CallbackFns.NoArgsVoidCallbackFunction;
import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Helpers.Direction;
import Maze.Candy.CandyRecord;
import Maze.Cell;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Maze;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;
import player.HumanPlayer.HumanPlayer;
import player.MoveStatus;
import player.exceptions.PlayerNotRunning;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static Logger.LoggerManager.logger;

public class MazePanel extends JPanel {

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
     * Set to 20 so it start with a little padding
     */
    private final int startX = 40;

    /**
     * From where to createRunningThread the maze Y axis
     * Set to 20 so it start with a little padding
     */
    private final int startY = 40;

    /**
     * From where to createRunningThread the maze X axis
     * Set to 20 so it start with a little padding
     */
    private final int cellVerMargin = 3;

    /**
     * From where to createRunningThread the maze Y axis
     * Set to 20 so it start with a little padding
     */
    private final int cellHorMargin = 3;

    /**
     * Subject to stop all the left subscriptions
     */
    private final Subject onDestroySub = PublishSubject.create();

    List<ControlledTimer> timeToLiveCandiesTimers;

    private ArrayList<NoArgsVoidCallbackFunction> startPlayersCallbacks;

    private BufferedImage exitArrowImage;
    private BufferedImage entranceArrowImage;

    private int arrowSize;

    private boolean currentlyOnPause = false;

    // region Constructors

    /**
     * Maze Preview Panel Base Constructor
     */
    public MazePanel() {
        init();
    }

    /**
     * Maze Preview Panel Constructor
     *
     * @param maze    Maze to build
     * @param players Players of the maze
     */
    public MazePanel(Maze maze, BasePlayer[] players) {
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
    public MazePanel(Maze maze, BasePlayer[] players, boolean atEntrances) {
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
    public MazePanel(Cell[][] cells, BasePlayer[] players) {
        this.maze = new Maze(cells);
        this.players = players;

        init();
    }

    private void init() {
        if (DebuggerHelper.isInDebugMode()) {
            this.initDebugging();
        }

        loadArrowIcons();

        this.setFocusable(true);
    }

    private void loadArrowIcons() {

        arrowSize = 15;
        URL exitImagePath = getClass().getResource("/maze/arrow-right-solid-exit.png");

        try {
            exitArrowImage = loadArrowIcon(exitImagePath);
        } catch (IOException e) {
            handleExceptionInLoadArrow(e);
            return;
        }

        URL entranceImagePath = getClass().getResource("/maze/arrow-right-solid-entrance.png");

        try {
            entranceArrowImage = loadArrowIcon(entranceImagePath);
        } catch (IOException e) {
            handleExceptionInLoadArrow(e);
            return;
        }
    }

    private void handleExceptionInLoadArrow(IOException e) {
        logger.error("[Load Image][Arrow Image]", e.getMessage());
        e.printStackTrace();

        // TODO - When Fail - show that can start the game or go to fallback
    }

    private BufferedImage loadArrowIcon(URL imagePath) throws IOException {

        BufferedImage arrowImage;

        arrowImage = loadImage(imagePath);

        // The arrow image is too big
        return resize(arrowImage, arrowSize, arrowSize);
    }

    private BufferedImage loadImage(URL imagePath) throws IOException {
        return ImageIO.read(imagePath);
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
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
        setBackground(background);

        this.startPlayersCallbacks = initPlayers();

        logger.debug("Don't forget to call `startGame()`");
    }

    public void startGame() {
        listenToPauseAction();

        runPlayers();

        startCandiesTimers();
    }

    private void listenToPauseAction() {
        Observable.merge(Arrays.stream(this.players).filter(Objects::nonNull).map(BasePlayer::getOnPlayerPauseObs).collect(Collectors.toList()))
                .takeUntil(onDestroySub)
                .subscribe(isPaused -> {
                    this.currentlyOnPause = (boolean) isPaused;

                    try {
                        if (this.currentlyOnPause) {
                            BasePlayer.pauseAllPlayers();
                            this.pauseAllTimeLimitedCandies();
                        } else {
                            BasePlayer.resumeAllPlayers();
                            this.resumeAllTimeLimitedCandies();
                        }
                    } catch (PlayerNotRunning playerNotRunning) {
                        playerNotRunning.printStackTrace();
                        logger.error("[OnPause][PlayerNotRunning]");
                    }
                });
    }

    private void pauseAllTimeLimitedCandies() {
        timeToLiveCandiesTimers.forEach(ControlledTimer::pause);
    }
    private void resumeAllTimeLimitedCandies() {
        timeToLiveCandiesTimers.forEach(ControlledTimer::resume);
    }

    private void runPlayers() {
        startPlayersCallbacks.forEach(NoArgsVoidCallbackFunction::run);
    }

    private void startCandiesTimers() {
        timeToLiveCandiesTimers = this.maze.getCandies()
                .stream()
                .filter(candyLoc -> candyLoc.candy.getTimeToLive() > 0)
                .map(this::createControlledTimerForTimeLimitedCandy)
                .collect(Collectors.toList());

        timeToLiveCandiesTimers.forEach(ControlledTimer::start);

    }

    private ControlledTimer createControlledTimerForTimeLimitedCandy(CandyRecord candyRecord) {
        ControlledTimer candyTimer = new ControlledTimer(candyRecord.candy.getTimeToLive(), () -> {
            this.maze.getCell(candyRecord.coordinate).removeCandy(candyRecord.candy);
        });

        onDestroySub.subscribe((b) -> {
            candyTimer.cancel();
        });

        return candyTimer;
    }

    private ArrayList<NoArgsVoidCallbackFunction> initPlayers() {
        ArrayList<NoArgsVoidCallbackFunction> startPlayersCallbacks = new ArrayList<>();

        for (BasePlayer player : this.players) {

            // Create entrance
            ELocation entrance = this.maze.getRandomEntrance();

            // Set default location to 0,0
            player.setLocation(entrance != null ? entrance.getLocation() : new Coordinate(0, 0));

            listenToPlayerMovement(player);

            if (player instanceof HumanPlayer) {
                startPlayersCallbacks.add(this.startHumanPlayer((HumanPlayer) player));
            } else if (player instanceof ComputerPlayer) {
                startPlayersCallbacks.add(startComputerPlayer((ComputerPlayer) player));
            }
        }

        return startPlayersCallbacks;
    }

    private void listenToPlayerMovement(BasePlayer player) {
        player.getPlayerMoveObs()
                .takeUntil(onDestroySub)
                .subscribe(direction -> {
                    logger.debug("[Player][OnMove] - Direction: " + direction);
                    MoveStatus res = this.movePlayer(player, (Direction) direction);
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
    }

    private NoArgsVoidCallbackFunction startComputerPlayer(ComputerPlayer player) {
        Thread playerThread = player.createRunningThread(this.maze, getExitForComputerPlayer(player));

        if (playerThread == null) {
            System.out.println("Player " + player.getName() + " can't start running");
            return null;
        }


        return playerThread::start;
    }

    private NoArgsVoidCallbackFunction startHumanPlayer(HumanPlayer player) {
        logger.debug("[Before Player Start]");
        this.addKeyListener(player);
        Thread playerThread = player.create();

        return playerThread::start;
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

        if (currentlyOnPause) {
            paintOverlay(g);
        }
    }


    private void paintOverlay(Graphics g) {
        Color before = g.getColor();

        int alpha = 127; // 50% transparent
        Color overlayColor = new Color(0, 0, 0, alpha);
        g.setColor(overlayColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(before);
    }

    private void paintMaze(Graphics g) {
        g.setColor(this.mazeColor);

        int fullW = getWidth() - (startX * 2);
        int fullH = getHeight() - (startY * 2);

        int horEdgeLen = fullW / this.maze.getWidth();
        int verEdgeLen = fullH / this.maze.getHeight();

        int topLeftX = startX;
        int topLeftY = startY;

        CellPainter.init(verEdgeLen, horEdgeLen, arrowSize, this::createArrow);

        for (int i = 0, h = this.maze.getHeight(), w = this.maze.getWidth(); i < h; i++) {
            for (int j = 0; j < w; j++) {
                CellPainter.paint(g, this.maze.getCell(i, j), topLeftX, topLeftY);
                topLeftX += horEdgeLen;
            }

            topLeftY += verEdgeLen;
            topLeftX = startX;
        }
    }

    private void createArrow(Graphics g, int angle, int x, int y, boolean isEntrance) {
        drawImageWithRotation((Graphics2D) g, isEntrance ? entranceArrowImage : exitArrowImage, angle, x, y);
    }

    private void drawImageWithRotation(Graphics2D g2d, BufferedImage image, int degree, int x, int y) {
        assert g2d != null && image != null;

        // The image not moved when the window resize

        AffineTransform affineTransform = AffineTransform.getTranslateInstance(x, y);

        // Multiplying by -1 to fix the bug that the image didn't pointed to the right direction
        // When the degree was 90 it rotated to bottom instead of top
        degree *= -1;

        affineTransform.rotate(Math.toRadians(degree), image.getWidth() / 2, image.getHeight() / 2);
        g2d.drawImage(image, affineTransform, this);
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

        if (this.maze.isValidMove(loc, direction)) {
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

    public void onFinishGame() {
        this.onDestroySub.onNext(true);
    }

    public Maze getMaze() {
        return maze;
    }
}
