package Maze;

import java.util.ArrayList;
import java.util.Stack;

import Helpers.Tuple;
import static Helpers.Utils.Instance;

/**
 * Maze
 */
public class Maze
{
    /**
     * The MazeData
     */
    private Cell[][] MazeData;

    public final Cell[][] getMazeData()
    {
        return MazeData;
    }

    public final void setMazeData(Cell[][] value)
    {
        MazeData = value;
    }

    /**
     * Create Rectangle MazeData
     *
     * @param width  Width Of The MazeData
     * @param height Height Of The Maz
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height)
    {
        this(width, height, 1, 1, 1);
    }

    /**
     * Create Rectangle MazeData
     *
     * @param width       Width Of The MazeData
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
     * Create Rectangle MazeData
     *
     * @param width            Width Of The MazeData
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
     * Create Rectangle MazeData
     *
     * @param width            Width Of The MazeData
     * @param height           Height Of The Maz
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @param numberOfExists   Number Of Exists
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int width, int height, int minDistance, int numberOfEntrance, int numberOfExists)
    {
        GenerateRectangleMaze(width, height, minDistance, numberOfEntrance, numberOfExists);
    }

    /**
     * Generate Rectangle MazeData
     *
     * @param width            Width Of The MazeData
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
     * Generate Rectangle MazeData
     *
     * @param width       Width Of The MazeData
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
     * Generate Rectangle MazeData
     *
     * @param width  Width Of The MazeData
     * @param height Height Of The Maz
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int width, int height)
    {
        GenerateRectangleMaze(width, height, 1, 1, 1);
    }

    /**
     * Generate Rectangle MazeData
     *
     * @param width            Width Of The MazeData
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

        // Init the MazeData with empty cubes
        InitMaze(height, width);

        int currRow = Instance.getRandomNumber(height), currCol = Instance.getRandomNumber(width);
        int maxCubes = width * height;
        int i = 0;

        // Queue for the steps made for knowing where to go
        // First value of the tuple is row and the second is column
        Stack<Tuple<Integer, Integer>> steps = new Stack<>();

        Direction nextDirection;

        // Get all direction values
        Direction[] directions = Direction.values();

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
     * Init MazeData with empty cubes
     *
     * @param height height of the MazeData
     * @param width  Width of the MazeData
     */
    private void InitMaze(int height, int width)
    {
        setMazeData(new Cell[height][width]);

        // Init the MazeData with empty cubes
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
        ArrayList<Direction> directionAvailable = new ArrayList<Direction>();
        Direction selected = null;
        int tempRow, tempCol;

        while (!directionAvailable.isEmpty()) {
            selected = directionAvailable.get(Instance.getRandomNumber(directionAvailable.size()));

            tempRow = currRow + Instance.getHorizontalDirection((Direction) selected);
            tempCol = currCol + Instance.getVerticalDirection((Direction) selected);

            if (Instance.inBounds(tempCol, tempRow, getMazeData()) && getMazeData()[currRow][currCol].setCellAtDirection(getMazeData()[tempRow][tempCol], selected, force, update)) {
                return selected;
            }

            directionAvailable.remove(selected);
            selected = null;
        }

        return null;
    }

    /**
     * Print Maze
     */
    public final void PrintMaze()
    {
        int rows = getMazeData().length;
        int columns = getMazeData()[0].length;

        String text = "";

        for (int y = 0; y < rows; y++) {
            String rowStr = "";
            for (int x = 0; x < columns; x++) {
                Cell cell = getMazeData()[y][x];

                rowStr += "{" +
                        "top: " + (cell.Top != null ? "true" : "false") +
                        ", bottom: " + (cell.Bottom != null ? "true" : "false") +
                        ", left: " + (cell.Left != null ? "true" : "false") +
                        ", right: " + (cell.Right != null ? "true" : "false") +
                        "},";
            }

            text += "[" + rowStr + "],";
        }

        System.out.println(text);
    }

}
