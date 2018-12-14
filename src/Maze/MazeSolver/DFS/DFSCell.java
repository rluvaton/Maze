package Maze.MazeSolver.DFS;

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


    /**
     * Gets a neighbour that could potentially be part of the solution path.
     *
     * @param top Top cell
     * @param right Right cell
     * @param bottom Bottom cell
     * @param left Left cell
     * @return the next cell location to go to
     */
    public Tuple<Integer, Integer> getPathNeighbour(DFSCell top, DFSCell right, DFSCell bottom, DFSCell left) {
        List<Tuple<Integer, Integer>> neighbours = new ArrayList<Tuple<Integer, Integer>>();

        if (top != null && !top.deadEnd && !this.haveTopWall()) {
            neighbours.add(new Tuple<>(-1, 0));
        }

        if (right != null && !right.deadEnd && !this.haveRightWall()) {
            neighbours.add(new Tuple<>(0, 1));
        }

        if (bottom != null && !bottom.deadEnd && !this.haveBottomWall()) {
            neighbours.add(new Tuple<>(1, 0));
        }

        if (left != null && !left.deadEnd && !this.haveLeftWall()) {
            neighbours.add(new Tuple<>(0, -1));
        }

        if (neighbours.size() == 0) {
            return null;
        }

        if (neighbours.size() == 1) {
            return neighbours.get(0);
        }

        return neighbours.get(Utils.Instance.getRandomNumber(neighbours.size()));
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
