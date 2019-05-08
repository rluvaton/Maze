package Maze;

import Helpers.*;
import Maze.Candy.*;
import Maze.Solver.Adapter.SolverAdapter;
import Maze.Solver.BFS.BFSSolverAdapter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private List<Tuple<Candy, Coordinate>> candies = new ArrayList<>();

    private SolverAdapter solverAdapter = new BFSSolverAdapter();

    // region Constructors

    /**
     * Create Maze from cells
     *
     * @param mazeData DFS cells
     */
    public Maze(Cell[][] mazeData) {
        assert mazeData != null;

        this.mazeData = mazeData;
        this.height = mazeData.length;

        if (mazeData.length > 0) {
            this.width = mazeData[0].length;
        }

        this.getELocationsFromMazeData();
        this.addToCandyList(true);
    }

    /**
     * Create Maze from cells
     *
     * @param mazeData DFS cells
     */
    public Maze(Cell[][] mazeData, List<ELocation> entrances, List<ELocation> exits) {
        this(mazeData);

        this.entrances = entrances;
        this.exits = exits;
    }

    // endregion

    private void getELocationsFromMazeData() {
        for (Cell[] row : this.mazeData) {
            for (Cell cell : row) {
                if(cell == null) {
                    continue;
                }

                Map<Direction, ELocation> eLocationNeighbors = cell.getELocationNeighbors();
                addELocationNeighborsToList(eLocationNeighbors);
            }
        }
    }

    private void addELocationNeighborsToList(Map<Direction, ELocation> eLocationNeighbors) {
        eLocationNeighbors.forEach((direction, eLocation) -> {
            ELocationType eLocationType = eLocation.getType();
            switch (eLocationType) {
                case Entrance:
                    this.entrances.add(eLocation);
                    break;
                case Exit:
                    this.exits.add(eLocation);
                    break;
                default:
                    break;
            }
        });
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
                        new Coordinate(
                                finalI,
                                finalJ))).collect(
                        Collectors.toList()));
            }
        }
    }

    // region Helpers

    /**
     * Check if valid move
     *
     * @param location  Location from where the move is made
     * @param direction Wanted direction
     * @return Return the direction of the move if it's valid
     */
    public Direction checkIfValidMove(Coordinate location, Direction direction) {
        Cell destCell = this.getCell(location);


        return (destCell == null || this.getCell(Instance.moveCoordinatesToDirection(location,
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
    public Cell checkIfValidMoveCell(Coordinate location, Direction direction) {
        Cell destCell = this.getCell(location);

        Cell nextCell = destCell != null ? this.getCell(Instance.moveCoordinatesToDirection(location, direction)) : null;

        return (destCell == null || nextCell == null || !destCell.haveCellAtDirection(
                direction)) ? null : nextCell;
    }

    /**
     * Check if valid location
     *
     * @param location Location to check it's validation
     * @return Return the result if location valid or not
     */
    public boolean isValidLocation(Coordinate location) {
        Cell destCell = this.getCell(location);

        return destCell != null;
    }

    /**
     * Check if valid location
     *
     * @param row Row to check
     * @param col Column to check
     * @return Return the result if location valid or not
     */
    public boolean isValidLocation(int row, int col) {
        Cell destCell = this.getCell(row, col);

        return destCell != null;
    }

    /**
     * Check if location and direction is an Exit / Entrance Location
     *
     * @param location  location
     * @param direction headed direction
     * @return Return the ELocation if found one
     */
    public ELocation checkIfELocation(Coordinate location, Direction direction) {
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
    public ELocation checkIfELocation(Coordinate location, Direction direction, ELocationType type) {
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

    public List<Tuple<Candy, Coordinate>> getCandies() {
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
                : entrances.get(RandomHelper.getRandomNumber(entrances.size()));
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
     * @param row    Row
     * @param column Column
     * @return Returns the cell
     */
    public Cell getCell(int row, int column) {
        if (row < 0 || row >= this.height || column < 0 || column >= this.width) {
            return null;
        }

        return this.mazeData[row][column];
    }

    /**
     * Get Maze cell at row & height
     *
     * @param location Location of the cell
     * @return Returns the cell
     */
    public Cell getCell(Coordinate location) {
        return location == null ? null : this.getCell(location.getRow(), location.getColumn());
    }

    public ELocation getExit(Coordinate location) {
        return location == null ? null : this.exits.stream().filter(exit -> location.equals(exit.getLocation())).findFirst().orElse(null);
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
