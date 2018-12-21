package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Candy.*;
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
    private List<Tuple<Integer, Integer>> entrances = new ArrayList<Tuple<Integer, Integer>>();

    /**
     * Exists Points
     */
    private List<Tuple<Integer, Integer>> exits = new ArrayList<Tuple<Integer, Integer>>();

    // region Constructors

    /**
     * Create Maze from cells
     *
     * @param mazeData DFS cells
     */
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

    // endregion

    // region Generate Rectangle Maze

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
    private void GenerateRectangleMaze(int height,
                                       int width,
                                       int minDistance,
                                       int numberOfEntrance,
                                       int numberOfExists) {
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

        this.generateRandomCandies((width * height) / 10);
    }

    // endregion

    /**
     * Remove the walls from the boundries in the exits
     *
     * @param loc    Cell location
     * @param height Maze height
     * @param width  Maze width
     */
    private void setExits(Tuple<Integer, Integer> loc, int height, int width) {
        // If the x value (item 1) in the cell is at the top maze
        if (loc.item1 == 0) {
            this.mazeData[loc.item1][loc.item2].setTopWall(false);
        }
        // If the x value (item 1) in the cell is at the bottom of the maze
        else if (loc.item1 == height - 1) {
            this.mazeData[loc.item1][loc.item2].setBottomWall(false);
        }
        // If the y value (item 2) in the cell is at the left maze
        else if (loc.item2 == 0) {
            this.mazeData[loc.item1][loc.item2].setLeftWall(false);
        }
        // If the y value (item 2) in the cell is at the right maze
        else if (loc.item2 == width - 1) {
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
    private Tuple<Integer, Integer> createUniqueLocation(List<Tuple<Integer, Integer>> entrances,
                                                         List<Tuple<Integer, Integer>> exits,
                                                         int minDistance,
                                                         boolean isExit) {
        // Generate random location that don't exist yet
        Tuple<Integer, Integer> tempLoc;
        boolean state = Instance.getRandomState();

        if (state) {
            tempLoc = new Tuple<>(Instance.getRandomNumber(height), Instance.getRandomNumber(2) * (width - 1));
        } else {
            tempLoc = new Tuple<>(Instance.getRandomNumber(2) * (height - 1), Instance.getRandomNumber(width));
        }

        Tuple<Integer, Integer>[] finalTempLoc = new Tuple[]{tempLoc};

        while ((entrances.size() != 0 && entrances.stream().anyMatch(loc -> loc.item1.equals(finalTempLoc[0].item1) && loc.item2.equals(
                finalTempLoc[0].item2)) && (isExit || exits.size() == 0 || exits.stream().allMatch(loc -> DFSSolver.getSolvePathDist(
                this.mazeData,
                loc,
                finalTempLoc[0]) >= minDistance))) || (exits.size() != 0 && exits.stream().anyMatch(loc -> loc.item1.equals(
                finalTempLoc[0].item1) && loc.item2.equals(finalTempLoc[0].item2)) && (!isExit || entrances.size() == 0 || entrances.stream().allMatch(
                loc -> DFSSolver.getSolvePathDist(this.mazeData, loc, finalTempLoc[0]) >= minDistance)))) {
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
    private Direction setCellAtRandomPlace(Tuple<Integer, Integer> loc,
                                           Direction[] directions,
                                           boolean force,
                                           boolean update) {
        ArrayList<Direction> directionAvailable = new ArrayList<>(Arrays.asList(directions));
        Direction selected;
        Tuple<Integer, Integer> nextLoc;
        int size = directionAvailable.size();

        while (size > 0) {
            selected = directionAvailable.get(Instance.getRandomNumber(size));

            nextLoc = Instance.getNextCell(loc, selected);

            if (Instance.inBounds(nextLoc,
                                  height,
                                  width) && this.mazeData[nextLoc.item1][nextLoc.item2].haveAllWalls() && this.mazeData[loc.item1][loc.item2].setCellAtDirection(
                    this.mazeData[nextLoc.item1][nextLoc.item2],
                    selected,
                    force,
                    update))
            {
                return selected;
            }

            directionAvailable.remove(selected);
            size--;
        }

        return null;
    }

    // region Candies

    // region Generate Random Candies

    /**
     * Generate Random candies that all of them are good
     *
     * @param count count of candies to generate
     */
    private void generateRandomCandies(int count) {
        this.generateRandomCandies(count, true);
    }

    /**
     * Generate random candies that if isAllGood property is false can be some bad candies too
     *
     * @param count            Count of candies to generate
     * @param generateOnlyGood Is all the candies that gonna be generated will be only good or to random
     */
    private void generateRandomCandies(int count, boolean generateOnlyGood) {
        Tuple<Integer, Integer> cellLoc;
        Cell cell;

        for (int i = 0; i < count; i++) {
            cellLoc = Utils.Instance.generateTuple(this.height, this.width);
            cell = this.getCell(cellLoc);

            cell.addCandy(this.generateSingleCandy(null,
                                                   new Tuple<>(!generateOnlyGood, true),
                                                   null,
                                                   null,
                                                   new Tuple<>(true, true),
                                                   null,
                                                   cellLoc));
        }
    }

    /**
     * Generate Random Candies that all of them are good
     *
     * @param pointsCount Count of candies that change the points count
     * @param timeCount   Count of candies that change the time that left
     * @param portalCount Count of candies that make the cell be a portal to another one
     */
    private void generateRandomCandies(int pointsCount, int timeCount, int portalCount) {
        this.generateRandomCandies(pointsCount, timeCount, portalCount, true);
    }

    /**
     * Generate Random Candies that all of them are good
     *
     * @param pointsCount      Count of candies that change the points count
     * @param timeCount        Count of candies that change the time that left
     * @param portalCount      Count of candies that make the cell be a portal to another one
     * @param generateOnlyGood Is all the candies that gonna be generated will be good
     */
    private void generateRandomCandies(int pointsCount, int timeCount, int portalCount, boolean generateOnlyGood) {
        Tuple<Integer, Integer> cellLoc;
        Cell cell;

        for (int i = 0; i < pointsCount; i++) {
            cellLoc = Utils.Instance.generateTuple(this.height, this.width);
            cell = this.getCell(cellLoc);

            cell.addCandy(this.generateSingleCandy(new CandyPowerType[]{CandyPowerType.Points},
                                                   new Tuple<>(!generateOnlyGood, true),
                                                   null,
                                                   null,
                                                   new Tuple<>(true, true),
                                                   null,
                                                   cellLoc));
        }

        for (int i = 0; i < timeCount; i++) {
            cellLoc = Utils.Instance.generateTuple(this.height, this.width);
            cell = this.getCell(cellLoc);

            cell.addCandy(this.generateSingleCandy(new CandyPowerType[]{CandyPowerType.Time},
                                                   new Tuple<>(!generateOnlyGood, true),
                                                   null,
                                                   null,
                                                   new Tuple<>(true, true),
                                                   null,
                                                   cellLoc));
        }

        for (int i = 0; i < portalCount; i++) {
            cellLoc = Utils.Instance.generateTuple(this.height, this.width);
            cell = this.getCell(cellLoc);

            cell.addCandy(this.generateSingleCandy(new CandyPowerType[]{CandyPowerType.Location},
                                                   new Tuple<>(!generateOnlyGood, true),
                                                   null,
                                                   null,
                                                   new Tuple<>(true, true),
                                                   null,
                                                   cellLoc));
        }
    }

    // endregion

    /**
     * Generate Single Candy, look at the function notes to understand how to pass the parameters value
     *
     * @param types             Candy available types, if null then it's all types
     * @param isGood            Is Good config
     * @param timeToLive        Time to live config
     * @param strengthPower     Strength power config
     * @param twoWayPortal      Two way portal config
     * @param otherCellLocation Other Cell Location config, in case of PortalCandy
     * @param cellLoc           Cell Location in case of PortalCandy with 2 way portal
     * @return Return the generated candy
     * @implNote In the function parameters all tuples built with first item be a boolean - whether or not to random this value
     * If the first value is true (you want predefine value) then item2 is the value that you want, or null for default
     * (for parameters that the second item is a tuple too than his first item is the value)
     * <p>
     * If the first value is false and the type of the second item is tuple than the tuple values will be the range for this property
     * @example I want to generate a candy that:
     * - isGood is true
     * - time to live is randomize between 1 to 5 seconds
     * - strengthPower is 5
     * - twoWayPortal is random
     * <p>
     * The function values will be
     * generateSingleCandy(new Tuple<>(false, true),
     * new Tuple<>(true, new Tuple<>(1000, 5000)),
     * new Tuple<>(false, new Tuple<>(5, 0)),
     * new Tuple<>(true, true));
     */
    private Candy generateSingleCandy(CandyPowerType[] types,
                                      Tuple<Boolean, Boolean> isGood,
                                      Tuple<Boolean, Tuple<Integer, Integer>> timeToLive,
                                      Tuple<Boolean, Tuple<Integer, Integer>> strengthPower,
                                      Tuple<Boolean, Boolean> twoWayPortal,
                                      Tuple<Boolean, Tuple<Integer, Integer>> otherCellLocation,
                                      Tuple<Integer, Integer> cellLoc) {
        // The type of the candy that gonna be generated
        CandyPowerType type;

        types = types != null ? types : CandyPowerType.values();

        type = types[Utils.Instance.getRandomNumber(types.length)];

        boolean isGoodVal = isGood == null || (isGood.item1 ? Utils.Instance.getRandomState() : isGood.item2);

        int timeToLiveVal = timeToLive == null ? -1 : timeToLive.item1 ? Utils.Instance.getRandomNumber(timeToLive.item2.item1,
                                                                                                        timeToLive.item2.item2) : timeToLive.item2 != null ? timeToLive.item2.item1 : -1;

        int strengthPowerVal = strengthPower == null ? 1000 : strengthPower.item1 ? Utils.Instance.getRandomNumber(
                strengthPower.item2.item1,
                strengthPower.item2.item2) : strengthPower.item2 != null ? strengthPower.item2.item1 : 1000;

        switch (type) {
            case Time:
                return new TimeCandy(isGoodVal, strengthPowerVal, timeToLiveVal);
            case Points:
                return new PointsCandy(isGoodVal, strengthPowerVal, timeToLiveVal);
            case Location:

                Tuple<Integer, Integer> otherCellLocationVal = otherCellLocation == null ? Utils.Instance.generateTuple(
                        height,
                        width) : otherCellLocation.item1 ? Utils.Instance.generateTuple(otherCellLocation.item2.item1,
                                                                                        otherCellLocation.item2.item2) : otherCellLocation.item2 != null ? otherCellLocation.item2 : Utils.Instance.generateTuple(
                        height,
                        width);

                boolean twoWayPortalVal = twoWayPortal == null || (twoWayPortal.item1 ? Utils.Instance.getRandomState() : twoWayPortal.item2);

                if (twoWayPortalVal) {
                    return new PortalCandy(isGoodVal,
                                           timeToLiveVal,
                                           otherCellLocationVal,
                                           this.getCell(otherCellLocationVal),
                                           cellLoc);
                } else {
                    return new PortalCandy(isGoodVal, timeToLiveVal, otherCellLocationVal, false);
                }

            default:
                return null;
        }
    }

    // endregion

    // region Getter & Setter

    /**
     * Get Random Entrence
     * @return The location of the entrence
     */
    public Tuple<Integer, Integer> getRandomEntrance() {
        return entrances == null || entrances.size() == 0 ? null : entrances.get(Instance.getRandomNumber(entrances.size()));
    }

    public List<Tuple<Integer, Integer>> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<Tuple<Integer, Integer>> entrances) {
        this.entrances = entrances;
    }

    public List<Tuple<Integer, Integer>> getExits() {
        return exits;
    }

    public void setExits(List<Tuple<Integer, Integer>> exits) {
        this.exits = exits;
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

    /**
     * Get Maze cell at row & height
     *
     * @param location Location of the cell, item1 is row item2 is column
     * @return Returns the cell
     */
    public Cell getCell(Tuple<Integer, Integer> location) {
        if (location == null || location.item1 < 0 || location.item1 >= this.height || location.item2 < 0 || location.item2 >= this.width) {
            return null;
        }

        return this.mazeData[location.item1][location.item2];
    }

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

    // endregion
}
