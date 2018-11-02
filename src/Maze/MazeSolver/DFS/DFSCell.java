package Maze.MazeSolver.DFS;

import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;

import java.util.ArrayList;
import java.util.List;

public class DFSCell extends Cell {
    private boolean deadEnd = false;


    /**
     * Gets a neighbour that could potentially be part of the solution path.
     * @param top
     * @param right
     * @param bottom
     * @param left
     * @return
     */
    public Tuple<Integer, Integer> getPathNeighbour(DFSCell top, DFSCell right, DFSCell bottom, DFSCell left) {
        List<Tuple<Integer, Integer>> neighbours = new ArrayList<Tuple<Integer, Integer>>();

        if (top != null) {
            if (!top.deadEnd) {
                if (!this.haveTopWall()) {
                    neighbours.add(new Tuple<>(-1, 0));
                }
            }
        }

        if (right != null) {
            if (!right.deadEnd) {
                if (!this.haveRightWall()) {
                    neighbours.add(new Tuple<>(0, 1));
                }
            }
        }

        if (bottom != null) {
            if (!bottom.deadEnd) {
                if (!this.haveBottomWall()) {
                    neighbours.add(new Tuple<>(1, 0));
                }
            }
        }

        if (left != null) {
            if (!left.deadEnd) {
                if (!this.haveLeftWall()) {
                    neighbours.add(new Tuple<>(0, -1));
                }
            }
        }

        if(neighbours.size() == 0) {
            return null;
        }

        if (neighbours.size() ==  1) {
            return neighbours.get(0);
        }

        return neighbours.get(Utils.Instance.getRandomNumber(neighbours.size()));
    }

    public boolean isDeadEnd() {
        return deadEnd;
    }

    public void setDeadEnd(boolean deadEnd) {
        this.deadEnd = deadEnd;
    }
}
