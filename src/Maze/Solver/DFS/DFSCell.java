package Maze.Solver.DFS;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * DFS Cell
 *
 * @description This is for solving a maze using DFS
 */
public class DFSCell extends Cell
{
    /**
     * Is the cell is a dead end
     */
    private boolean deadEnd = false;

    public DFSCell(Cell cell) {
        super(cell);
    }

    /**
     * Gets a neighbour that could potentially be part of the solution path.
     *
     * @param top Top cell
     * @param right Right cell
     * @param bottom Bottom cell
     * @param left Left cell
     * @return PathNeighbourResult that contain the next cell location to go to with that direction
     */
    public PathNeighbourResult getPathNeighbour(DFSCell top, DFSCell right, DFSCell bottom, DFSCell left) {
        List<PathNeighbourResult> neighbours = new ArrayList<>();

        if (top != null && !top.deadEnd && this.haveCellAtDirection(Direction.TOP)) {
            neighbours.add(new PathNeighbourResult(top, Direction.TOP));
        }

        if (right != null && !right.deadEnd && this.haveCellAtDirection(Direction.RIGHT)) {
            neighbours.add(new PathNeighbourResult(right, Direction.RIGHT));
        }

        if (bottom != null && !bottom.deadEnd && this.haveCellAtDirection(Direction.BOTTOM)) {
            neighbours.add(new PathNeighbourResult(bottom, Direction.BOTTOM));
        }

        if (left != null && !left.deadEnd && this.haveCellAtDirection(Direction.LEFT)) {
            neighbours.add(new PathNeighbourResult(left, Direction.LEFT));
        }

        if (neighbours.size() == 0) {
            return null;
        }

        if (neighbours.size() == 1) {
            return neighbours.get(0);
        }

        return neighbours.get(Utils.Instance.getRandomNumber(neighbours.size()));
    }

    public static DFSCell createFromCell(Cell cell) {
        return new DFSCell(cell);
    }

    // region Getter & Setter

    public boolean isDeadEnd() {
        return deadEnd;
    }

    public void setDeadEnd(boolean deadEnd) {
        this.deadEnd = deadEnd;
    }

    // endregion
}
