package Maze;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Node;
import Maze.Candy.*;

import java.util.*;
import java.util.stream.Collectors;

import static Helpers.Utils.Instance;


/**
 * Cell in maze
 */
public class Cell extends Node<Coordinate> {

    // region Variables

    /**
     * If the cell contain candy
     */
    private ArrayList<Candy> candies = new ArrayList<>();

    /**
     * The location is also the ID
     */
    protected Coordinate location;

    private Map<Direction, NeighborCell> neighbors = Cell.createEmptyNeighborsMap();

    private static Map<Direction, NeighborCell> createEmptyNeighborsMap() {
        Map<Direction, NeighborCell> neighbors = new HashMap<>();

        neighbors.put(Direction.TOP, null);
        neighbors.put(Direction.RIGHT, null);
        neighbors.put(Direction.BOTTOM, null);
        neighbors.put(Direction.LEFT, null);

        return neighbors;
    }

    // endregion

    // region Constructors

    public Cell(Cell cell) {
        super(cell.id);

        // TODO - WHEN CELL PROPERTIES UPDATED DON'T FORGET TO UPDATE THIS TOO

        this.location = this.id;
        this.neighbors = Instance.cloneMap(cell.neighbors, new HashMap<>());
        this.candies = cell.candies;
    }

    public Cell(int row, int col) {
        this(new Coordinate(row, col));
    }

    public Cell(Coordinate position) {
        super(position.clone());

        this.location = id;
    }

    // endregion

    @Override
    public boolean equals(Node<Coordinate> cell) {
        return cell != null && this.id.equals(cell.id);
    }

    public boolean haveCellOrELocationAtDirection(Direction direction) {
        assert direction != null;
        return this.getCellAtDirection(direction) != null || this.ELocationAtDirection(direction) != null;
   }

    /**
     * Get Cell at direction
     *
     * @param direction Direction of where to get the cube
     * @return Returns the cube at the requested direction
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean haveCellAtDirection(Direction direction) {
        assert direction != null;
        return this.getCellAtDirection(direction) != null;
    }

    // region Set Cell At Direction

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @param force     If to force the change (can delete other cell direction)
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Direction direction, Cell cell, boolean force) throws Exception {
        return setCellAtDirection(direction, cell, force, false);
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Direction direction, Cell cell) throws Exception {
        return setCellAtDirection(direction, cell, false, true);
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @param force     If to force the change (can delete other cell direction)
     * @param update    If need to update the cell
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Direction direction, Cell cell, boolean force, boolean update) throws Exception {
        if (!update && cell == null) {
            throw new IllegalArgumentException("cell");
        }

        if(Instance.getDirectionOfMove(this.location, cell.location) != direction) {
            // TODO - change to NotNeighborException or something
            throw new Exception("not Near");
        }

        if (this.haveCellAtDirection(direction) && !force) {
            return false;
        }

        this._setCellAtDirection(direction, cell);

        if (update) {
            cell._setCellAtDirection(Direction.getOppositeDirection(direction), this);
        }

        return true;
    }

    // endregion

    public void setELocationAsNeighbor(ELocation elocation) {

        NeighborCell neighborCell = this.neighbors.get(elocation.getDirection());
        if(neighborCell == null) {
            neighborCell = new NeighborCell(null, elocation);
        }
        this.neighbors.put(elocation.getDirection(), neighborCell);
    }

    public void removeELocationFromNeighbors(Direction direction) {
        this.neighbors.get(direction).eLocation = null;
    }

    public Map<Direction, ELocation> getELocationNeighbors() {
        Map<Direction, ELocation> eLocationNeighbors = new HashMap<>();

        neighbors.forEach((direction, neighborCell) -> {
            if(neighborCell != null && neighborCell.eLocation != null) {
                eLocationNeighbors.put(direction, neighborCell.eLocation);
            }
        });

        return eLocationNeighbors;

    }
    /**
     * If Have all walls
     *
     * @return Returns if have all walls
     */
    public boolean haveAllWalls() {
        return this.neighbors.values().stream().allMatch(Objects::isNull);
    }

    // region Candies

    public void addCandy(Candy candy) {
        this.candies.add(candy);
    }

    public void addCandies(Candy[] candies) {
        this.candies.addAll(List.of(candies));
    }

    public void removeCandy(Candy candy) {
        this.candies.remove(candy);
    }

    // region Time Candies

    /**
     * Collect Time Candies and remove them from the cell candies
     *
     * @return Returns all the time candies in this cell
     * @see #collectTimeCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public List<TimeCandy> collectTimeCandy() {
        return this.collectTimeCandy(true);
    }

    /**
     * Collect Time Candies
     *
     * @param removeFoundedCandies Do you want to remove the founded time candies?
     * @return Returns all the time candies in this cell
     * @see #collectTimeCandy() For option to collect & remove candies from cell
     */
    public List<TimeCandy> collectTimeCandy(boolean removeFoundedCandies) {
        List<TimeCandy> timeCandies = this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof TimeCandy ||
                        candy.getType() ==
                                CandyPowerType.Time))
                .map(candy -> (TimeCandy) candy)
                .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return timeCandies;
        }

        timeCandies.forEach(timeCandy -> this.candies.remove(timeCandy));

        return timeCandies;
    }

    /**
     * Collect and sum the Time Candies Strength and remove them from the cell
     *
     * @return Returns the sum of the strengths of the time candies
     * @see #collectTimeCandyStrengths(boolean removeFoundedCandies) For option to not remove the candies from to cell
     */
    public int collectTimeCandyStrengths() {
        return this.collectTimeCandyStrengths(true);
    }

    /**
     * Collect Time Candies Strength
     *
     * @param removeFoundedCandies Do you want to remove the founded time candies?
     * @return Returns the sum of the strengths of the time candies
     * @see #collectTimeCandyStrengths() For option to collect & remove time candies from cell
     */
    public int collectTimeCandyStrengths(boolean removeFoundedCandies) {
        List<TimeCandy> timeCandies = this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof TimeCandy ||
                        candy.getType() ==
                                CandyPowerType.Time))
                .map(candy -> (TimeCandy) candy)
                .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return calculateCandiesTotalStrength(timeCandies);
        }

        timeCandies.forEach(timeCandy -> this.candies.remove(timeCandy));

        return calculateCandiesTotalStrength(timeCandies);
    }

    private int calculateCandiesTotalStrength(List<TimeCandy> timeCandies) {
        return timeCandies.stream()
                .mapToInt(Candy::getCandyStrength)
                .reduce(Integer::sum)
                .orElse(0);
    }

    // endregion

    // region Points Candies

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public List<PointsCandy> collectPointsCandy() {
        return this.collectPointsCandy(true);
    }

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @param removeFoundedCandies Do you want to remove the founded points candies?
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandy() For option to collect & remove candies from cell
     */
    public List<PointsCandy> collectPointsCandy(boolean removeFoundedCandies) {
        List<PointsCandy> pointsCandies = this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof PointsCandy ||
                        candy.getType() ==
                                CandyPowerType.Points))
                .map(candy -> (PointsCandy) candy)
                .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return pointsCandies;
        }

        pointsCandies.forEach(pointCandy -> this.candies.remove(pointCandy));

        return pointsCandies;
    }

    /**
     * Collect and sum the Time Candies Strength and remove them from the cell
     *
     * @return Returns the sum of the strengths of the points candies in this cell
     * @see #collectPointsCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public int collectPointsCandyStrengths() {
        return this.collectPointsCandyStrengths(true);
    }

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @param removeFoundedCandies Do you want to remove the founded points candies?
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandyStrengths() For option to collect & remove candies from cell
     */
    public int collectPointsCandyStrengths(boolean removeFoundedCandies) {
        List<PointsCandy> pointsCandies = this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof PointsCandy ||
                        candy.getType() ==
                                CandyPowerType.Points))
                .map(candy -> (PointsCandy) candy)
                .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return pointsCandies.stream()
                    .mapToInt(Candy::getCandyStrength)
                    .reduce((candy1, candy2) -> candy1 + candy2)
                    .orElse(0);
        }

        pointsCandies.forEach(pointCandy -> this.candies.remove(pointCandy));

        return pointsCandies.stream()
                .mapToInt(Candy::getCandyStrength)
                .reduce((candy1, candy2) -> candy1 + candy2)
                .orElse(0);
    }

    // endregion Points Candies

    /**
     * Get the Location (Portal) Candy
     *
     * @return Returns the first Location (Portal) candy that founded
     * @implNote It doesn't remove the candy
     * @see #collectLocationCandyPortal For getting the candy tranfer location
     */
    public PortalCandy collectLocationCandy() {
        return (PortalCandy) this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof PortalCandy ||
                        candy.getType() == CandyPowerType.Location))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the Location that the Location (Portal) Candy transfer you
     *
     * @return Returns Location that the first Location (Portal) candy that founded
     * @implNote It doesn't remove the candy
     * @see #collectLocationCandy For getting the location candy
     */
    public Coordinate collectLocationCandyPortal() {
        return this.candies.stream()
                .filter(candy -> candy != null && (candy instanceof PortalCandy ||
                        candy.getType() == CandyPowerType.Location))
                .findFirst()
                .map(candy -> (PortalCandy) candy)
                .map(PortalCandy::getLocation)
                .orElse(null);
    }

    // endregion

    public Cell getCellAtDirection(Direction dir) {
        NeighborCell neighborCell = this.neighbors.getOrDefault(dir, null);
        return neighborCell == null ? null : neighborCell.getCell();
    }

    public ELocation ELocationAtDirection(Direction dir) {
        NeighborCell neighborCell = this.neighbors.getOrDefault(dir, null);
        return neighborCell == null ? null : neighborCell.eLocation;
    }

    private void _setCellAtDirection(Direction dir, Cell cell) {
        Cell currentNeighbor = this.getCellAtDirection(dir);
        if(currentNeighbor != null && currentNeighbor != cell) {
            currentNeighbor._setCellAtDirection(Direction.getOppositeDirection(dir), null);
        }
        this.neighbors.put(dir, new NeighborCell(cell));
    }

    // region Getter & Setter

    public ArrayList<Candy> getCandies() {
        return this.candies;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        if (location == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        this.location = location;
        this.id = this.location;
    }

    public Map<Direction, NeighborCell> getNeighbors() {
        return neighbors;
    }

    public static class NeighborCell {
        private Cell cell;
        private ELocation eLocation;

        public NeighborCell(Cell cell) {
            this.cell = cell;
        }

        public NeighborCell(Cell cell, ELocation eLocation) {
            this.cell = cell;
            this.eLocation = eLocation;
        }

        public Cell getCell() {
            return cell;
        }
    }

    // endregion
}
