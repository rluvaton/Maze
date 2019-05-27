package Maze.Solver.DFS;

import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Cell;

/**
 * The response object from {@link DFSCell#getPathNeighbour getPathNeighbour function}
 */
public class PathNeighbourResult {
    /**
     * The Neighbour cell
     */
    public Cell cell;

    /**
     * where to go on the horizontal and vertical space
     */
    public Coordinate stepAction;

    /**
     * Direction of the cell
     */
    public Direction direction;

    public PathNeighbourResult(Cell cell, Direction direction) {
        this.cell = cell;
        this.direction = direction;
        this.stepAction = direction.getValue();
    }

    public PathNeighbourResult(Cell cell, Coordinate stepAction, Direction direction) {
        this.cell = cell;
        this.stepAction = stepAction;
        this.direction = direction;
    }

    /**
     * Get next location from some location with the step action
     * @param currentLocation Current to location to go from
     * @return Return the location
     */
    public Coordinate getNextLocation(Coordinate currentLocation) {
        return new Coordinate(currentLocation.getRow() + this.stepAction.getRow(), currentLocation.getColumn() + this.stepAction.getColumn());

    }
}
