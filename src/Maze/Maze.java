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

        // Queue for the steps made for knowing where to go
        // First value of the tuple is row and the second is column
        Stack<Tuple<Integer, Integer>> steps = new Stack<>();

        Direction nextDirection;

        // Get all direction values
        Direction[] directions = Direction.values();

        Tuple<Integer, Integer> currLoc = new Tuple<>(currRow, currCol);

        steps.push(currLoc);
        while (!steps.empty()) {
            nextDirection = setCellAtRandomPlace(currLoc, directions, false, true);

            if (nextDirection == null) {
                currLoc = steps.pop();
            } else {
                steps.push(currLoc);
                currLoc = Instance.getNextCell(currLoc, nextDirection);
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
     * @param loc        Tuple of the cell location in the matrix
     * @param directions Available directions
     * @param force      Force Changes
     * @param update     Update The direction cube
     * @return Returns the direction that selected
     */
    private Direction setCellAtRandomPlace(Tuple<Integer, Integer> loc, Direction[] directions, boolean force, boolean update)
    {
        ArrayList<Direction> directionAvailable = new ArrayList<>(Arrays.asList(directions));
        Direction selected = null;
        Tuple<Integer, Integer> nextLoc;

        while (!directionAvailable.isEmpty()) {
            selected = directionAvailable.get(Instance.getRandomNumber(directionAvailable.size()));

            nextLoc = Instance.getNextCell(loc, selected);

            if(Instance.inBounds(nextLoc, height, width)) {
                System.out.println("row: " + nextLoc.item1 + ", col: " +  nextLoc.item2 + ", Direction: " + selected + ", res: " + this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls());
            }

            if (Instance.inBounds(nextLoc, height, width) &&
                    this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls() &&
                    this.mazeData[loc.item1][loc.item2].setCellAtDirection(this.mazeData[nextLoc.item1][nextLoc.item2], selected, force, update))
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
