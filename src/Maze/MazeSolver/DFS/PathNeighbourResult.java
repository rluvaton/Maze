package Maze.MazeSolver.DFS;

import Helpers.Direction;
import Helpers.Tuple;
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
    public Tuple<Integer, Integer> stepAction;

    /**
     * Direction of the cell
     */
    public Direction direction;

    public PathNeighbourResult(Cell cell, Tuple<Integer, Integer> stepAction, Direction direction) {
        this.cell = cell;
        this.stepAction = stepAction;
        this.direction = direction;
    }

    /**
     * Get next location from some location with the step action
     * @param currentLocation Current to location to go from
     * @return Return the location
     */
    public Tuple<Integer, Integer> getNextLocation(Tuple<Integer, Integer> currentLocation) {
        return new Tuple<>(currentLocation.item1 + this.stepAction.item1, currentLocation.item2 + this.stepAction.item2);

    }
}
