package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import Helpers.Tuple;
import Maze.MazeSolver.DFS.DFSCell;
import Maze.MazeSolver.DFS.DFSSolver;

import static Helpers.Utils.Instance;

/**
 * Maze
 */
public class Maze
{
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


    /**
     * Entrances Points
     */
    List<Tuple<Integer, Integer>> entrances = new ArrayList<Tuple<Integer, Integer>>();

    /**
     * Exists Points
     */
    List<Tuple<Integer, Integer>> exits = new ArrayList<Tuple<Integer, Integer>>();

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

        minDistance = (minDistance <= 0) ? 1 : minDistance;

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

        // List of entrances and exists
        List<Tuple<Integer, Integer>> entrances = new ArrayList<Tuple<Integer, Integer>>();
        List<Tuple<Integer, Integer>> exits = new ArrayList<Tuple<Integer, Integer>>();

        while (entrances.size() < numberOfEntrance || exits.size() < numberOfExists) {
            if (entrances.size() < numberOfEntrance) {

                // Generate random location that don't exist yet
                entrances.add(createUniqueLocation(entrances, exits, minDistance, false));
            }

            if (exits.size() < numberOfExists) {
                exits.add(createUniqueLocation(entrances, exits, minDistance, true));
            }
        }

        this.entrances = entrances;
        this.exits = exits;

        this.entrances.forEach(loc -> setExits(loc, height, width));
        this.exits.forEach(loc -> setExits(loc, height, width));
    }

    private void setExits(Tuple<Integer, Integer> loc, int height, int width) {
        if (loc.item1 == 0) {
                this.mazeData[loc.item1][loc.item2].setTopWall(false);
            } else if (loc.item1 == height - 1) {
                this.mazeData[loc.item1][loc.item2].setBottomWall(false);
            } else if (loc.item2 == 0) {
                this.mazeData[loc.item1][loc.item2].setLeftWall(false);
            } else if (loc.item2 == width - 1) {
                this.mazeData[loc.item1][loc.item2].setRightWall(false);
            }
    }

    /**
     * Generate Unique Location that doesn't exist in the entrances or exists
     *
     * @param entrances   Entrances location list
     * @param exits       Exits Location list
     * @param minDistance Min Distance
     * @param isExit      Trying to create unique location to
     * @return Return the unique location
     */
    private Tuple<Integer, Integer> createUniqueLocation(List<Tuple<Integer, Integer>> entrances, List<Tuple<Integer, Integer>> exits, int minDistance, boolean isExit) {
        // Generate random location that don't exist yet
        Tuple<Integer, Integer> tempLoc;
        boolean state = Instance.getRandomState();

        if (state) {
            tempLoc = new Tuple<>(Instance.getRandomNumber(height), Instance.getRandomNumber(2) * (width - 1));
        } else {
            tempLoc = new Tuple<>(Instance.getRandomNumber(2) * (height - 1), Instance.getRandomNumber(width));
        }

        Tuple[] finalTempLoc = new Tuple[]{tempLoc};

        while (
                (entrances.size() != 0 &&
                        entrances.stream().anyMatch(loc ->
                                loc.item1.equals(finalTempLoc[0].item1) &&
                                        loc.item2.equals(finalTempLoc[0].item2)) &&
                        (isExit || exits.size() == 0 || exits.stream().allMatch(loc -> DFSSolver.getSolvePathDist(this.mazeData, loc, finalTempLoc[0]) >= minDistance))) ||
                        (exits.size() != 0 &&
                                exits.stream().anyMatch(loc ->
                                        loc.item1.equals(finalTempLoc[0].item1) &&
                                                loc.item2.equals(finalTempLoc[0].item2)) &&
                                (!isExit || entrances.size() == 0 || entrances.stream().allMatch(loc -> DFSSolver.getSolvePathDist(this.mazeData, loc, finalTempLoc[0]) >= minDistance)))) {
            state = Instance.getRandomState();

            if (state) {
                tempLoc = new Tuple<>(Instance.getRandomNumber(height), Instance.getRandomNumber(2) * (width - 1));
            } else {
                tempLoc = new Tuple<>(Instance.getRandomNumber(2) * (height - 1), Instance.getRandomNumber(width));
            }
            finalTempLoc[0] = tempLoc;
        }

        return tempLoc;
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

            if (Instance.inBounds(nextLoc, height, width) &&
                    this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls() &&
                    this.mazeData[loc.item1][loc.item2].setCellAtDirection(this.mazeData[nextLoc.item1][nextLoc.item2], selected, force, update))
            {
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
