package Maze.Solver.DFS;

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

        if (top != null && !top.deadEnd && !this.haveTopWall()) {
            neighbours.add(new PathNeighbourResult(top, new Tuple<>(-1, 0), Direction.TOP));
        }

        if (right != null && !right.deadEnd && !this.haveRightWall()) {
            neighbours.add(new PathNeighbourResult(right, new Tuple<>(0, 1), Direction.RIGHT));
        }

        if (bottom != null && !bottom.deadEnd && !this.haveBottomWall()) {
            neighbours.add(new PathNeighbourResult(bottom, new Tuple<>(1, 0), Direction.BOTTOM));
        }

        if (left != null && !left.deadEnd && !this.haveLeftWall()) {
            neighbours.add(new PathNeighbourResult(left, new Tuple<>(0, -1), Direction.LEFT));
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
