package Maze;

import java.util.*;
import java.util.stream.Collectors;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Candy.*;
import Maze.Solver.Adapter.SolverAdapter;
import Maze.Solver.DFS.DFSSolverAdapter;

import static Helpers.Utils.Instance;

/**
 * Maze
 */
public class Maze {
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

    /**
     * Entrances Points
     */
    private List<ELocation> entrances = new ArrayList<ELocation>();

    /**
     * Exists Points
     */
    private List<ELocation> exits = new ArrayList<ELocation>();

    /**
     * List of candy and his location
     */
    private List<Tuple<Candy, Tuple<Integer, Integer>>> candies = new ArrayList<>();

    private SolverAdapter solverAdapter = new DFSSolverAdapter();

    // region Constructors

    /**
     * Create Maze from cells
     *
     * @param mazeData DFS cells
     */
    public Maze(Cell[][] mazeData) {
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
     * @param minDistance Minimum Distance (number of cubes) between the createRunningThread and the end
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
     * @param minDistance      Minimum Distance (number of cubes) between the createRunningThread and the end
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
     * @param minDistance      Minimum Distance (number of cubes) between the createRunningThread and the end
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

    // region Maze Init

    // region Generate Rectangle Maze

    /**
     * Generate Rectangle mazeData
     *
     * @param height           Height Of The Maz
     * @param width            Width Of The mazeData
     * @param minDistance      Minimum Distance (number of cubes) between the createRunningThread and the end
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
     * @param minDistance Minimum Distance (number of cubes) between the createRunningThread and the end
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
     * @param minDistance      Minimum Distance (number of cubes) between the createRunningThread and the end
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

        // Push to the createRunningThread of the steps
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

        this.generateRandomCandies((width * height) / 10, false);

        // Add candies to the candy list
        this.addToCandyList(true);

        this.generateELocations(numberOfEntrance, numberOfExists, minDistance);
    }

    // endregion

    /**
     * Generate Entrances and Exits
     *
     * @param numberOfEntrance Number of Maze Entrances
     * @param numberOfExists   Number of maze Exists
     * @param minDistance      Minimum Distance between each entrance and exit
     */
    private void generateELocations(int numberOfEntrance, int numberOfExists, int minDistance) {

        // List of entrances and exists
        List<Tuple<Integer, Integer>> entrances = new ArrayList<Tuple<Integer, Integer>>();
        List<Tuple<Integer, Integer>> exits = new ArrayList<Tuple<Integer, Integer>>();

        while (entrances.size() < numberOfEntrance || exits.size() < numberOfExists) {
            if (entrances.size() < numberOfEntrance) {

                // Generate random location that don't exist yet
                entrances.add(createUniqueLocation(entrances, exits, minDistance, false, true));
            }

            if (exits.size() < numberOfExists) {
                exits.add(createUniqueLocation(entrances, exits, minDistance, true, true));
            }
        }

        this.entrances = entrances.stream().map(loc -> createELocations(loc,
                height,
                width,
                ELocationType.Entrance)).collect(Collectors.toList());

        this.exits = exits.stream().map(loc -> createELocations(loc, height, width, ELocationType.Exit)).collect(
                Collectors.toList());
    }

    /**
     * Add the candies that are in the cells to the list of all candies and their location
     *
     * @param removeAll Remove all candies in ${@link #candies}
     */
    private void addToCandyList(boolean removeAll) {
        if (removeAll) {
            this.candies.clear();
        }

        // Add candies to the candy list
        for (int i = 0, len = this.mazeData.length; i < len; i++) {
            for (int j = 0, rowLen = this.mazeData[i].length; j < rowLen; j++) {
                int finalI = i;
                int finalJ = j;
                this.candies.addAll(this.mazeData[i][j].getCandies().stream().map(candy -> new Tuple<>(candy,
                        new Tuple<>(
                                finalI,
                                finalJ))).collect(
                        Collectors.toList()));
            }
        }
    }

    // endregion

    /**
     * Create ELocations (Enter / Exit Locations)
     * Remove the walls from the boundaries in the entrances / exits
     *
     * @param loc    Exit / Enter location
     * @param height Maze height
     * @param width  Maze width
     * @return ELocation of the exit / enter location
     */
    private ELocation createELocations(Tuple<Integer, Integer> loc, int height, int width, ELocationType type) {
        Direction dir = null;

        // If the x value (item 1) in the cell is at the top maze
        if (loc.item1 == 0) {
            this.mazeData[loc.item1][loc.item2].setTopWall(false);
            dir = Direction.TOP;
        }
        // If the x value (item 1) in the cell is at the bottom of the maze
        else if (loc.item1 == height - 1) {
            this.mazeData[loc.item1][loc.item2].setBottomWall(false);
            dir = Direction.BOTTOM;
        }
        // If the y value (item 2) in the cell is at the left maze
        else if (loc.item2 == 0) {
            this.mazeData[loc.item1][loc.item2].setLeftWall(false);
            dir = Direction.LEFT;
        }
        // If the y value (item 2) in the cell is at the right maze
        else if (loc.item2 == width - 1) {
            this.mazeData[loc.item1][loc.item2].setRightWall(false);
            dir = Direction.RIGHT;
        } else {
            System.out.println("The Enter / Exit location is not at the maze's borders");
        }

        return new ELocation(loc, dir, type);
    }

    /**
     * Generate Unique Location that doesn't exist in the entrances or exists
     *
     * @param entrances           Entrances location list
     * @param exits               Exits Location list
     * @param minDistance         Min Distance
     * @param isExit              Trying to create unique location to
     * @param withTeleportCandies If when calculation minimum distance we take into account the teleport candies
     * @return Return the unique location
     */
    private Tuple<Integer, Integer> createUniqueLocation(List<Tuple<Integer, Integer>> entrances,
                                                         List<Tuple<Integer, Integer>> exits,
                                                         int minDistance,
                                                         boolean isExit,
                                                         boolean withTeleportCandies) {
        // Generate random location that don't exist yet
        Tuple<Integer, Integer> tempLoc;

        if (Instance.getRandomState()) {
            tempLoc = new Tuple<>(Instance.getRandomNumber(height), Instance.getRandomNumber(2) * (width - 1));
        } else {
            tempLoc = new Tuple<>(Instance.getRandomNumber(2) * (height - 1), Instance.getRandomNumber(width));
        }

        // Final for the lambda functions
        Tuple<Integer, Integer>[] finalTempLoc = new Tuple[]{tempLoc};


        while (
                (
                        // While entrances is empty
                        entrances.size() != 0 &&
                                // And there already an entrance with the randomised location
                                entrances.stream().anyMatch(loc -> Tuple.compare(loc, finalTempLoc[0])) &&
                                // And either the wanted ELocation is an exit
                                (isExit ||
                                        // Or the wanted location is entrance but there are no exits so there is no min distance to check
                                        exits.size() == 0 ||
                                        // Or wanted location is entrance and not all the exits path to the randomised entrance is bigger than the minimum distance
                                        !exits.stream().allMatch(loc -> {
                                            try {
                                                return solverAdapter.solveMaze(this, loc, finalTempLoc[0], withTeleportCandies).length >= minDistance;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                return false;
                                            }
                                        }))) ||
                        // Or while exits is empty
                        (exits.size() != 0 &&
                                // And there already an exit with the randomised location
                                exits.stream().anyMatch(loc -> Tuple.compare(loc, finalTempLoc[0])) &&
                                // And either the wanted ELocation is entrance
                                (!isExit ||
                                        // Or the wanted location is exit but there are no entrances so there is no min distance to check
                                        entrances.size() == 0 ||
                                        // Or wanted location is exit and not all the entrances path to the randomised exit is bigger than the minimum distance
                                        !entrances.stream().allMatch(loc -> {
                                            try {
                                                return solverAdapter.solveMaze(this, loc, finalTempLoc[0], withTeleportCandies).length >= minDistance;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                return false;
                                            }
                                        })))) {

            if (Instance.getRandomState()) {
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
        setMazeData(new Cell[height][width]);

        // Init the mazeData with empty cubes
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                getMazeData()[i][j] = new Cell(i, j);
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
                    update)) {
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
                    new Tuple<>(Instance.getRandomState(), null),
                    null,
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
     * @param timeToLive        Time to live config none at default, the random value to create random between is in seconds if provided tuple of random time is between 1 to 20 seconds
     * @param strengthPower     Strength power config
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
                                      Tuple<Boolean, Tuple<Integer, Integer>> otherCellLocation,
                                      Tuple<Integer, Integer> cellLoc) {
        // The type of the candy that gonna be generated
        CandyPowerType type;

        types = types != null ? types : CandyPowerType.values();

        type = types[Utils.Instance.getRandomNumber(types.length)];

        boolean isGoodVal = isGood == null || (isGood.item1 ? Utils.Instance.getRandomState() : isGood.item2);

        // In case of timeToLive is null then it will set no time to live,
        int timeToLiveVal;
        if (timeToLive == null) timeToLiveVal = -1;
        else timeToLiveVal = timeToLive.item1
                ? ((timeToLive.item2 == null)
                ? (Utils.Instance.getRandomNumber(1,
                20) * 1000)
                : (Utils.Instance.getRandomNumber(timeToLive.item2.item1 == null
                        ? 1
                        : timeToLive.item2.item1,
                timeToLive.item2.item2 == null
                        ? 20
                        : timeToLive.item2.item2) * 1000))
                : ((timeToLive.item2 != null) ? timeToLive.item2.item1 : -1);

        int strengthPowerVal = strengthPower == null
                ? 1000
                : strengthPower.item1
                ? Utils.Instance.getRandomNumber(strengthPower.item2.item1,
                strengthPower.item2.item2)
                : strengthPower.item2 != null ? strengthPower.item2.item1 : 1000;

        switch (type) {
            case Time:
                return new TimeCandy(isGoodVal, strengthPowerVal, timeToLiveVal);
            case Points:
                return new PointsCandy(isGoodVal, strengthPowerVal, timeToLiveVal);
            case Location:

                Tuple<Integer, Integer> otherCellLocationVal = otherCellLocation == null
                        ? Utils.Instance.generateTuple(height,
                        width)
                        : otherCellLocation.item1
                        ? otherCellLocation.item2.clone()
                        : otherCellLocation.item2 != null
                        ? otherCellLocation.item2
                        : Utils.Instance.generateTuple(height, width);

                return new PortalCandy(timeToLiveVal,
                        otherCellLocationVal,
                        this.getCell(otherCellLocationVal),
                        cellLoc);

            default:
                return null;
        }
    }

    // endregion

    // region Helpers

    /**
     * Check if valid move
     *
     * @param location  Location from where the move is made
     * @param direction Wanted direction
     * @return Return the direction of the move if it's valid
     */
    public Direction checkIfValidMove(Tuple<Integer, Integer> location, Direction direction) {
        Cell destCell = this.getCell(location);


        return (destCell == null || this.getCell(Instance.getNextCell(location,
                direction)) == null || !destCell.haveCellAtDirection(
                direction)) ? null : direction;
    }

    /**
     * Check if valid move and return the cell if valid
     *
     * @param location  Location from where the move is made
     * @param direction Wanted direction
     * @return Return the cell of the move if it's valid
     */
    public Cell checkIfValidMoveCell(Tuple<Integer, Integer> location, Direction direction) {
        Cell destCell = this.getCell(location);

        Cell nextCell = destCell != null ? this.getCell(Instance.getNextCell(location, direction)) : null;

        return (destCell == null || nextCell == null || !destCell.haveCellAtDirection(
                direction)) ? null : nextCell;
    }

    /**
     * Check if valid location
     *
     * @param location Location to check it's validation
     * @return Return the result if location valid or not
     */
    public boolean checkIfValidLocation(Tuple<Integer, Integer> location) {
        Cell destCell = this.getCell(location);

        return destCell != null;
    }

    /**
     * Check if location and direction is an Exit / Entrance Location
     *
     * @param location  location
     * @param direction headed direction
     * @return Return the ELocation if found one
     */
    public ELocation checkIfELocation(Tuple<Integer, Integer> location, Direction direction) {
        Optional<ELocation> opELoc;

        opELoc = exits.stream().filter(exitLoc -> exitLoc.isAtELocation(location, direction)).findFirst();

        if (opELoc.isPresent()) {
            return opELoc.get();
        }

        opELoc = entrances.stream().filter(enterLoc -> enterLoc.isAtELocation(location, direction)).findFirst();

        return opELoc.orElse(null);
    }

    /**
     * Check if location and direction is an Exit/Enter Location
     *
     * @param location  location
     * @param direction headed direction
     * @param type      Type of ELocation to search (null will search all)
     * @return Return the ELocation if found one
     * @description More efficient if you search only specific type (enter / exit)
     */
    public ELocation checkIfELocation(Tuple<Integer, Integer> location, Direction direction, ELocationType type) {
        Optional<ELocation> opELoc;

        if (type == null) {
            return this.checkIfELocation(location, direction);
        }

        List<ELocation> search = (type == ELocationType.Exit) ? exits : entrances;

        opELoc = search.stream().filter(exitLoc -> exitLoc.isAtELocation(location, direction)).findFirst();

        return opELoc.orElse(null);
    }

    // endregion

    // region Getter & Setter

    public List<Tuple<Candy, Tuple<Integer, Integer>>> getCandies() {
        return candies;
    }

    /**
     * Get Random Entrance
     *
     * @return The location of the entrance
     */
    public ELocation getRandomEntrance() {
        return entrances == null || entrances.size() == 0
                ? null
                : entrances.get(Instance.getRandomNumber(entrances.size()));
    }

    public List<ELocation> getEntrances() {
        return entrances;
    }

    public void setEntrances(List<ELocation> entrances) {
        this.entrances = entrances;
    }

    public List<ELocation> getExits() {
        return exits;
    }

    public void setExits(List<ELocation> exits) {
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

    public SolverAdapter getSolverAdapter() {
        return solverAdapter;
    }

    public void setSolverAdapter(SolverAdapter solverAdapter) {
        this.solverAdapter = solverAdapter;
    }

    // endregion
}
