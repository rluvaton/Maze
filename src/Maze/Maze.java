package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import Helpers.Tuple;

import static Helpers.Utils.Instance;

/**
 * Maze
 */
public class Maze
{
    /**
     * The mazeData
     */
    private Cell[][] mazeData;

    /**
     * Width of the maze
     */
    private int height;

    /**
     * Height of the maze
     */
    private int width;

    public Maze(Cell[][] mazeData)
    {
        this.mazeData = mazeData;
    }

    /**
     * Create Rectangle mazeData
     *
     * @param width  Width Of The mazeData
     * @param height Height Of The Maz
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height)
    {
        this(width, height, 1, 1, 1);
    }

    /**
     * Create Rectangle mazeData
     *
     * @param width       Width Of The mazeData
     * @param height      Height Of The Maz
     * @param minDistance Minimum Distance (number of cubes) between the start and the end
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height, int minDistance)
    {
        this(width, height, minDistance, 1, 1);
    }

    /**
     * Create Rectangle mazeData
     *
     * @param width            Width Of The mazeData
     * @param height           Height Of The Maz
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height, int minDistance, int numberOfEntrance)
    {
        this(width, height, minDistance, numberOfEntrance, 1);
    }


    /**
     * Create Rectangle mazeData
     *
     * @param width            Width Of The mazeData
     * @param height           Height Of The Maz
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @param numberOfExists   Number Of Exists
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height, int minDistance, int numberOfEntrance, int numberOfExists)
    {
        this.width = width;
        this.height = height;

        GenerateRectangleMaze(width, height, minDistance, numberOfEntrance, numberOfExists);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param width            Width Of The mazeData
     * @param height           Height Of The Maz
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */

    private void GenerateRectangleMaze(int width, int height, int minDistance, int numberOfEntrance)
    {
        GenerateRectangleMaze(width, height, minDistance, numberOfEntrance, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param width       Width Of The mazeData
     * @param height      Height Of The Maz
     * @param minDistance Minimum Distance (number of cubes) between the start and the end
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int width, int height, int minDistance)
    {
        GenerateRectangleMaze(width, height, minDistance, 1, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param width  Width Of The mazeData
     * @param height Height Of The Maz
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int width, int height)
    {
        GenerateRectangleMaze(width, height, 1, 1, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param width            Width Of The mazeData
     * @param height           Height Of The Maz
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @param numberOfExists   Number Of Exists
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int width, int height, int minDistance, int numberOfEntrance, int numberOfExists)
    {
        // It's not possible to make distance more than the width * height
        if (minDistance > width * height) {
            throw new IllegalArgumentException("minDistance");
        }

        // Init the mazeData with empty cubes
        InitMaze(height, width);

        int currRow = Instance.getRandomNumber(height), currCol = Instance.getRandomNumber(width);
        int maxCubes = width * height - 1;
        int i = 0;

        // Queue for the steps made for knowing where to go
        // First value of the tuple is row and the second is column
        Stack<Tuple<Integer, Integer>> steps = new Stack<>();

        Direction nextDirection;

        // Get all direction values
        Direction[] directions = Direction.values();

        // SET -1 cause it crashed at the end
        while (i < maxCubes) {
            nextDirection = setCellAtRandomPlace(currRow, currCol, directions, false, true);

            if (nextDirection == null) {
                if (steps.empty()) {
                    System.out.println("No Direction Founded and there are no prev steps are");
                    throw new RuntimeException("No Direction Founded and there are no prev steps are");
                }

                Tuple<Integer, Integer> prevLoc = steps.pop();

                currRow = prevLoc.item1;
                currCol = prevLoc.item2;
            } else {
                steps.push(new Tuple<>(currRow, currCol));
                currRow += Instance.getHorizontalDirection(nextDirection);
                currCol += Instance.getVerticalDirection(nextDirection);

                // Another Cube Added
                i++;
            }
        }
    }

    /**
     * Init mazeData with empty cubes
     *
     * @param height height of the mazeData
     * @param width  Width of the mazeData
     */
    private void InitMaze(int height, int width)
    {
        setMazeData(new Cell[height][width]);

        // Init the mazeData with empty cubes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getMazeData()[i][j] = new Cell();
            }
        }
    }

    /**
     * Set Cube At Random Place
     *
     * @param currRow    Current cube row
     * @param currCol    Current cube column
     * @param directions Available directions
     * @return Returns the direction that selected
     */
    private Direction setCellAtRandomPlace(int currRow, int currCol, Direction[] directions)
    {
        return setCellAtRandomPlace(currRow, currCol, directions, false, false);
    }

    /**
     * Set Cube At Random Place
     *
     * @param currRow    Current cube row
     * @param currCol    Current cube column
     * @param directions Available directions
     * @param force      Force Changes
     * @return Returns the direction that selected
     */
    private Direction setCellAtRandomPlace(int currRow, int currCol, Direction[] directions, boolean force)
    {
        return setCellAtRandomPlace(currRow, currCol, directions, force, false);
    }

    /**
     * Set Cube At Random Place
     *
     * @param currRow    Current cube row
     * @param currCol    Current cube column
     * @param directions Available directions
     * @param force      Force Changes
     * @param update     Update The direction cube
     * @return Returns the direction that selected
     */
    private Direction setCellAtRandomPlace(int currRow, int currCol, Direction[] directions, boolean force, boolean update)
    {
        ArrayList<Direction> directionAvailable = new ArrayList<>(Arrays.asList(directions));
        Direction selected = null;
        int tempRow, tempCol;

        while (!directionAvailable.isEmpty()) {
            selected = directionAvailable.get(Instance.getRandomNumber(directionAvailable.size()));

            tempRow = currRow + Instance.getHorizontalDirection((Direction) selected);
            tempCol = currCol + Instance.getVerticalDirection((Direction) selected);

            if(Instance.inBounds(tempRow, tempCol, height, width)) {
                System.out.println("row: " + tempRow + ", col: " +  tempCol + ", Direction: " + selected + ", res: " + this.mazeData[tempRow][tempCol].haveAllWalls());
            }

            if (Instance.inBounds(tempRow, tempCol, height, width) &&
                    this.mazeData[tempRow][tempCol].haveAllWalls() &&
                    this.mazeData[currRow][currCol].setCellAtDirection(this.mazeData[tempRow][tempCol], selected, force, update))
            {
                return selected;
            }

            directionAvailable.remove(selected);
        }

        return null;
    }

    // region Getter & Setter

    public Cell[][] getMazeData() {
        return mazeData;
    }

    public void setMazeData(Cell[][] mazeData) {
        this.mazeData = mazeData;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Get Maze cell at row & height
     *
     * @param i height
     * @param j row
     * @return Returns the cell
     */
    public Cell getCellAt(int i, int j) {
        return this.mazeData[i][j];
    }

    // endregion
}
