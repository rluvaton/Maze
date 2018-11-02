package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import Helpers.Tuple;
import Maze.MazeSolver.DFS.DFSCell;
import Maze.MazeSolver.DFS.DFSSolver;

import static Helpers.Utils.Instance;

/**
 * Maze
 */
public class Maze {
    /**
     * The mazeData
     */
    private DFSCell[][] mazeData;

    /**
     * Width of the maze
     */
    private int height;

    /**
     * Height of the maze
     */
    private int width;

    public Maze(DFSCell[][] mazeData) {
        this.mazeData = mazeData;
    }

    /**
     * Create Rectangle mazeData
     *
     * @param height Height Of The Maz
     * @param width  Width Of The mazeData
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int height, int width) {
        this(height, width, 1, 1, 1);
    }

    /**
     * Create Rectangle mazeData
     *
     * @param height      Height Of The Maz
     * @param width       Width Of The mazeData
     * @param minDistance Minimum Distance (number of cubes) between the start and the end
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int height, int width, int minDistance) {
        this(height, width, minDistance, 1, 1);
    }

    /**
     * Create Rectangle mazeData
     *
     * @param height           Height Of The Maz
     * @param width            Width Of The mazeData
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int height, int width, int minDistance, int numberOfEntrance) {
        this(height, width, minDistance, numberOfEntrance, 1);
    }


    /**
     * Create Rectangle mazeData
     *
     * @param height           Height Of The Maz
     * @param width            Width Of The mazeData
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @param numberOfExists   Number Of Exists
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    public Maze(int height, int width, int minDistance, int numberOfEntrance, int numberOfExists) {
        this.height = height;
        this.width = width;

        GenerateRectangleMaze(height, width, minDistance, numberOfEntrance, numberOfExists);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param height           Height Of The Maz
     * @param width            Width Of The mazeData
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */

    private void GenerateRectangleMaze(int height, int width, int minDistance, int numberOfEntrance) {
        GenerateRectangleMaze(height, width, minDistance, numberOfEntrance, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param height      Height Of The Maz
     * @param width       Width Of The mazeData
     * @param minDistance Minimum Distance (number of cubes) between the start and the end
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int height, int width, int minDistance) {
        GenerateRectangleMaze(height, width, minDistance, 1, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param height Height Of The Maz
     * @param width  Width Of The mazeData
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int height, int width) {
        GenerateRectangleMaze(height, width, 1, 1, 1);
    }

    /**
     * Generate Rectangle mazeData
     *
     * @param height           Height Of The Maz
     * @param width            Width Of The mazeData
     * @param minDistance      Minimum Distance (number of cubes) between the start and the end
     * @param numberOfEntrance Number Of Starting Points
     * @param numberOfExists   Number Of Exists
     * @throws IllegalArgumentException Throw error if distance more bigger then width * height
     * @throws RuntimeException         Throw error if next direction is null and steps are empty
     */
    private void GenerateRectangleMaze(int height, int width, int minDistance, int numberOfEntrance, int numberOfExists) {
        int maxCells = width * height;

        // It's not possible to make distance more than the width * height
        if (minDistance > maxCells) {
            throw new IllegalArgumentException("minDistance");
        }

        int visited = 0;

        // Init the mazeData with empty cubes
        InitMaze(height, width);

        // Queue for the steps made for knowing where to go
        // First value of the tuple is row and the second is column
        Stack<Tuple<Integer, Integer>> steps = new Stack<>();

        Direction nextDirection;

        // Get all direction values
        Direction[] directions = Direction.values();

        // Create Tuple of the current location
        Tuple<Integer, Integer> currLoc = new Tuple<>(Instance.getRandomNumber(height),
                Instance.getRandomNumber(width));

        // Push to the start of the steps
        steps.push(currLoc);
        visited++;

        while (visited < maxCells) {
            // NOTE - I can improve the maze generation by checking if all the cells are visited so I don't need to
            // return back at the end, I just compare the number of visited to the max cells that can be

            nextDirection = setCellAtRandomPlace(currLoc, directions, false, true);

            if (nextDirection == null) {
                currLoc = steps.pop();
            } else {
                visited++;
                steps.push(currLoc);
                currLoc = Instance.getNextCell(currLoc, nextDirection);
            }
        }

        // TODO - Random the start and end point

        // Uncomment this to calculate min distance between each entrance and exit,
        // (change the tuples to be the start and end location)
        // DFSSolver.getSolvePathDist(this.mazeData, new Tuple<>(0, 0), new Tuple<>(5, 1))


    }

    /**
     * Init mazeData with empty cubes
     *
     * @param height height of the mazeData
     * @param width  Width of the mazeData
     */
    private void InitMaze(int height, int width) {
        setMazeData(new DFSCell[height][width]);

        // Init the mazeData with empty cubes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getMazeData()[i][j] = new DFSCell();
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
    private Direction setCellAtRandomPlace(Tuple<Integer, Integer> loc, Direction[] directions, boolean force, boolean update) {
        ArrayList<Direction> directionAvailable = new ArrayList<>(Arrays.asList(directions));
        Direction selected = null;
        Tuple<Integer, Integer> nextLoc;
        int size = directionAvailable.size();

        while (size > 0) {
            selected = directionAvailable.get(Instance.getRandomNumber(size));

            nextLoc = Instance.getNextCell(loc, selected);

            if (Instance.inBounds(nextLoc, height, width)) {
                System.out.println("row: " + nextLoc.item1 + ", col: " + nextLoc.item2 + ", Direction: " + selected + ", res: " + this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls());
            }

            if (Instance.inBounds(nextLoc, height, width) &&
                    this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls() &&
                    this.mazeData[loc.item1][loc.item2].setCellAtDirection(this.mazeData[nextLoc.item1][nextLoc.item2], selected, force, update)) {
                return selected;
            }

            directionAvailable.remove(selected);
            size--;
        }

        return null;
    }

    // region Getter & Setter

    public DFSCell[][] getMazeData() {
        return mazeData;
    }

    public void setMazeData(DFSCell[][] mazeData) {
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
