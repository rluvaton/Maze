package GUI.MazeGame;

import Helpers.Direction;
import Logger.LoggerManager;
import Maze.Candy.Candy;
import Maze.Cell;
import Maze.ELocation;
import Maze.ELocationType;
import player.exceptions.InvalidDirectionException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class CellPainter {
    private static int verticalEdgeLen = -1;
    private static int horizontalEdgeLen = -1;
    private static int arrowSize = -1;

    private static CreateArrowFn createArrowFn;
    private static boolean initialized = false;

    private Graphics g;
    private Cell paintedCell;
    private int topLeftX;
    private int topLeftY;

    public static void init(int verticalEdgeLen, int horizontalEdgeLen, int arrowSize, CreateArrowFn createArrowFn) {
        CellPainter.verticalEdgeLen = verticalEdgeLen;
        CellPainter.horizontalEdgeLen = horizontalEdgeLen;
        CellPainter.arrowSize = arrowSize;
        CellPainter.createArrowFn = createArrowFn;

        initialized = true;
    }

    public static void paint(Graphics g, Cell cell, int topLeftX, int topLeftY) {
        if (!initialized) {
            throw new ExceptionInInitializerError("Please Init the Cell painter by calling `CellPainter.init(<verticalEdgeLen>, <horizontalEdgeLen>)` before creating");
        }

        new CellPainter(g, cell, topLeftX, topLeftY).paint();
    }

    private CellPainter(Graphics g, Cell paintedCell, int topLeftX, int topLeftY) {
        this.g = g;
        this.paintedCell = paintedCell;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
    }

    public void paint() {
        paintWalls();
        paintELocations();
        paintCandiesInCell();

    }

    private void paintWalls() {
        if (haveWallAtDirection(Direction.UP)) {
            drawTopWall();
        }

        if (haveWallAtDirection(Direction.DOWN)) {
            drawBottomWall();
        }

        if (haveWallAtDirection(Direction.RIGHT)) {
            drawRightWall();
        }

        if (haveWallAtDirection(Direction.LEFT)) {
            drawLeftWall();
        }
    }

    private boolean haveWallAtDirection(Direction up) {
        return !paintedCell.haveCellOrELocationAtDirection(up);
    }

    private void drawTopWall() {
        drawWall(topLeftX, topLeftY, topLeftX + horizontalEdgeLen, topLeftY);
    }

    private void drawBottomWall() {
        drawWall(topLeftX, topLeftY + verticalEdgeLen, topLeftX + horizontalEdgeLen, topLeftY + verticalEdgeLen);
    }

    private void drawRightWall() {
        drawWall(topLeftX + horizontalEdgeLen, topLeftY, topLeftX + horizontalEdgeLen, topLeftY + verticalEdgeLen);
    }

    private void drawLeftWall() {
        drawWall(topLeftX, topLeftY, topLeftX, topLeftY + verticalEdgeLen);
    }

    private void drawWall(int topLeftX, int topLeftY, int i, int topLeftY2) {
        g.drawLine(topLeftX, topLeftY, i, topLeftY2);
    }

    private void paintELocations() {
        Set<Map.Entry<Direction, ELocation>> elocationEntries = paintedCell.getELocationNeighbors().entrySet();

        ArrowData arrowData;

        for (Map.Entry<Direction, ELocation> entry : elocationEntries) {

            arrowData = new ArrowData(entry.getValue().getType(), entry.getKey());

            try {
                arrowData = getPointForArrow(arrowData);
            } catch (InvalidDirectionException e) {
                LoggerManager.logger.error("[PaintELocation][Invalid Direction] ", e.getMessage());
                continue;
            }

            createArrowFn.createArrow(g, arrowData.getDirectionBasedOnELocationType().getAngle(), arrowData.x, arrowData.y, arrowData.type == ELocationType.Entrance);
        }
    }

    private ArrowData getPointForArrow(ArrowData arrowData) throws InvalidDirectionException {
        int x;
        int y;

        int arrowPaddingBeforeMaze = 5;
        switch (arrowData.direction) {
            case UP:
                x = topLeftX + (Math.abs(arrowSize - horizontalEdgeLen)) / 2;
                y = topLeftY - arrowSize - arrowPaddingBeforeMaze;
                break;
            case DOWN:
                x = topLeftX + (Math.abs(arrowSize - horizontalEdgeLen)) / 2;
                y = topLeftY + verticalEdgeLen + arrowPaddingBeforeMaze;
                break;
            case RIGHT:
                x = topLeftX + horizontalEdgeLen + arrowPaddingBeforeMaze;
                y = topLeftY + (Math.abs(arrowSize - verticalEdgeLen)) / 2;
                break;
            case LEFT:
                x = topLeftX - arrowSize - arrowPaddingBeforeMaze;
                y = topLeftY + (Math.abs(arrowSize - verticalEdgeLen)) / 2;
                break;
            default:
                throw new InvalidDirectionException(arrowData.direction);
        }

        return arrowData.setPoint(x, y);
    }

    private void paintCandiesInCell() {
        Color before = g.getColor();

        ArrayList<Candy> cellCandies = (ArrayList<Candy>) paintedCell.getCandies().clone();

        if (!cellCandies.isEmpty()) {
            for (Candy candy : cellCandies) {
                if (candy == null) {
                    continue;
                }

                g.setColor(Color.decode(candy.getColor()));
                g.drawOval(topLeftX + horizontalEdgeLen / 2, topLeftY + verticalEdgeLen / 2, horizontalEdgeLen / 5, verticalEdgeLen / 5);
            }
        }

        g.setColor(before);
    }

}

