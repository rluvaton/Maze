package Maze.Solver.BFS;

import Maze.Cell;

public class BFSCell extends Cell {

    private boolean isVisited = false;

    public BFSCell(Cell cell) {
        super(cell);
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
