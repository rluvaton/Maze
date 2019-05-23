package GUI.MazeGame;

import Game.GameState;
import Game.MazeGame;
import Game.MovementListenerManager;
import Helpers.Coordinate;
import Helpers.DebuggerHelper;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Maze.Maze;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.BasePlayer;
import player.HumanPlayer.HumanPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static Logger.LoggerManager.logger;

public class MazePanel extends JPanel {

    private static final int DEFAULT_CELL_EDGE = 25;

    private MazeGame game;

    /**
     * Background Color
     */
    private Color background = Color.WHITE;

    /**
     * Maze Color, The colors of the maze's borders
     */
    private Color mazeColor = Color.BLUE;

    /**
     * From where to start the maze X axis so it start with a little padding
     */
    private final int startX = 40;

    /**
     * From where to start the maze Y axis so it start with a little padding
     */
    private final int startY = 40;

    /**
     * From where the player will start with vertical padding
     */
    private final int cellVerPadding = 5;

    /**
     * From where the player will start with horizontal padding
     */
    private final int cellHorPadding = 5;

    /**
     * Cell horizontal length (from right to left)
     */
    private int cellHorizontalLength = 10;

    /**
     * Cell vertical size (from top to bottom)
     */
    private int cellVerticalLength = 10;

    /**
     * Subject to stop all the left subscriptions
     */
    private final Subject onDestroySub = PublishSubject.create();

    private BufferedImage exitArrowImage;
    private BufferedImage entranceArrowImage;

    private int arrowSize;

    private boolean currentlyOnPause = false;

    public MazePanel(MazeGame game) {
        ObjectAssertion.requireNonNull(game, "game can't be null");

        this.game = game;
        init();
    }

    private void init() {
        if (DebuggerHelper.isInDebugMode()) {
            this.initDebugging();
        }

        loadArrowIcons();

        this.setFocusable(true);

        game.getOnFinishGameObs().subscribe(
                basePlayer -> onDestroySub.onNext(true),
                throwable -> onDestroySub.onNext(true)
        );
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

        game.initGame(new MovementListenerManager() {
            @Override
            public void addListenerForPlayer(HumanPlayer player) {
                ObjectAssertion.requireNonNull(player, "player can't be null when adding movement listener");
                addKeyListener(player);
            }

            @Override
            public void removeListenerForPlayer(HumanPlayer player) {
                ObjectAssertion.requireNonNull(player, "player can't be null when removing movement listener");
                removeKeyListener(player);
            }
        });

        logger.debug("[MazePanel][Reminder] Don't forget to call `startGame()`");
    }

    public void startGame() {
        game.startGame();
        setFocusable(true);
    }

    // region GUI Painting

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.paintMaze(g);
        this.showPlayers(g);

        if (game.getGameState() == GameState.PAUSE) {
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

        Dimension mazeDimension = getMazeDimension();

        int fullW = getWidth() - (startX * 2);
        int fullH = getHeight() - (startY * 2);

        Maze maze = this.game.getMaze();

        cellHorizontalLength = fullW / mazeDimension.width;
        cellVerticalLength = fullH / mazeDimension.height;

        int topLeftX = startX;
        int topLeftY = startY;

        CellPainter.init(cellVerticalLength, cellHorizontalLength, arrowSize, this::createArrow);

        for (int i = 0; i < mazeDimension.height; i++) {
            for (int j = 0; j < mazeDimension.width; j++) {
                CellPainter.paint(g, maze.getCell(i, j), topLeftX, topLeftY);
                topLeftX += cellHorizontalLength;
            }

            topLeftY += cellVerticalLength;
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

        Dimension mazeDimension = getMazeDimension();

        int horSpace = fullW / mazeDimension.width;
        int verSpace = fullH / mazeDimension.height;

        List<BasePlayer> players = this.game.getPlayers();

        for (BasePlayer player : players) {
            if (player.getColor() != null) {
                g.setColor(player.getColor().getColor());
            }

            Coordinate coordinates = this.calculateLocation(player.getLocation());

            g.drawOval(coordinates.getRow() + this.cellHorPadding,
                    coordinates.getColumn() + this.cellVerPadding,
                    horSpace - this.cellHorPadding,
                    verSpace - this.cellVerPadding);

            g.fillOval(coordinates.getRow() + this.cellHorPadding,
                    coordinates.getColumn() + this.cellVerPadding,
                    horSpace - this.cellHorPadding,
                    verSpace - this.cellVerPadding);
            repaint();
        }

        g.setColor(before);
    }


    /**
     * Calculate location in the panel
     *
     * @param location Location in the matrix
     * @return The x, y coordinates of the location in the panel
     */
    private Coordinate calculateLocation(Coordinate location) {

        int fullW = getWidth() - startX * 2;
        int fullH = getHeight() - startY * 2;

        Dimension mazeDimension = getMazeDimension();

        int horSpace = fullW / mazeDimension.width;
        int verSpace = fullH / mazeDimension.height;

        return new Coordinate(startX + horSpace * location.getColumn(), startY + verSpace * location.getRow());
    }

    // endregion

    public void onFinishGame() {
        this.onDestroySub.onNext(true);
        game.onFinishGame();
    }

    public Observable<BasePlayer> getOnFinishGameObs() {
        return game.getOnFinishGameObs();
    }

    public Dimension getMazeDimension() {
        return new Dimension(this.game.getMazeWidth(), this.game.getMazeHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension mazeDim = getMazeDimension();

        return new Dimension(
                DEFAULT_CELL_EDGE * mazeDim.width + 2 * startX,
                DEFAULT_CELL_EDGE * mazeDim.height + 2 * startY
        );
    }
}
