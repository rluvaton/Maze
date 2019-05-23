package Maze.Solver.DFS;


import Helpers.Coordinate;
import Helpers.Direction;
import Maze.Cell;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Maze;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.function.Function;

public class DFSSolverAdapter extends SolverAdapter {

    /**
     * Solve Maze
     *
     * @param maze        Maze to solve
     * @param start       Starting location to solve from
     * @param end         Ending Location to solve from
     * @param withCandies Solve maze with candies
     * @return Steps (Direction) of the path that solved the maze
     * @throws Exception When the maze can't be solved
     */
    @Override
    public Direction[] solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception {
        Stack<Coordinate> path = new Stack<>();
        ArrayList<Direction> steps = new ArrayList<>();

        Coordinate current = start;
        PathNeighbourResult pathNeighbourResult;
        Coordinate maybeNextLoc;
        ELocation eLocation;

        DFSCell[][] dfsCells = convertMazeCellsToBFSCells(maze.getMazeData());
        DFSCell tempCell;

        int iterations = 0;
        int maxTries = maze.getHeight() * maze.getWidth() + 5;

        while (!Coordinate.equals(current, end) && iterations < maxTries) {
            tempCell = this.getDFSCellFromCell(dfsCells, maze.getCell(current));
            tempCell.setDeadEnd(true);

            pathNeighbourResult = tempCell.getPathNeighbour(
                    this.getDFSCellFromCell(dfsCells, maze.checkIfValidMoveCell(current, Direction.UP)),
                    this.getDFSCellFromCell(dfsCells, maze.checkIfValidMoveCell(current, Direction.RIGHT)),
                    this.getDFSCellFromCell(dfsCells, maze.checkIfValidMoveCell(current, Direction.DOWN)),
                    this.getDFSCellFromCell(dfsCells, maze.checkIfValidMoveCell(current, Direction.LEFT)));

            if (pathNeighbourResult != null) {
                iterations++;
                path.push(current);

                eLocation = (pathNeighbourResult.cell == null) ? maze.checkIfELocation(current, pathNeighbourResult.direction) : null;

                if (eLocation != null && eLocation.getType() == ELocationType.Exit) {
                    current = pathNeighbourResult.getNextLocation(current);
                    continue;
                } else if (eLocation != null && eLocation.getType() == ELocationType.Entrance) {
                    // You don't need to enter the entrance
                    continue;
                } else if (pathNeighbourResult.cell == null) {
                    // If it don't have next cell and not entrance / exit than it's an error with the maze generation
                    throw new Exception("There is an error with the maze generation");
                }

                maybeNextLoc = withCandies ? pathNeighbourResult.cell.collectLocationCandyPortal() : null;
                if (maybeNextLoc != null) {
                    current = maybeNextLoc.clone();
                } else {
                    current = pathNeighbourResult.getNextLocation(current);
                }
                steps.add(pathNeighbourResult.direction);
            } else if (!path.isEmpty()) {
                try {
                    current = path.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // TODO - CHECK IF THE STEPS ARRAY IS REALLY THE PATH STEPS WITHOUT USELESS STEPS
        return (iterations >= maxTries) ? null : steps.toArray(new Direction[0]);
    }
    /**
     * Solve Maze
     *
     * @param maze        Maze to solve
     * @param start       Starting location to solve from
     * @param end         Ending Location to solve from
     * @param withCandies Solve maze with candies
     * @return Steps (Direction) of the path that solved the maze
     * @throws Exception When the maze can't be solved
     */
    @Override
    public Direction[] solveMaze(Cell[][] maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception {
        throw new UnsupportedOperationException("solveMaze with cell matrix");
    }

    /**
     * Solve Maze
     *
     * @param maze         Maze to solve
     * @param start        Starting location to solve from
     * @param end          Ending Location to solve from
     * @param withCandies  Solve maze with candies
     * @param stepCallback Callback for each step that occurred
     * @return Direction of the path that solved
     * @throws Exception When the maze can't be solved
     */
    @Override
    public Direction[] solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies, Function<Direction, Void> stepCallback) throws Exception {
        return new Direction[0];
    }

    private DFSCell[][] convertMazeCellsToBFSCells(Cell[][] cells) {
        return Arrays.stream(cells).map(cellsRow -> Arrays.stream(cellsRow).map(DFSCell::createFromCell).toArray(DFSCell[]::new)).toArray(DFSCell[][]::new);
    }

    private DFSCell getDFSCellFromCell(DFSCell[][] cells, Cell cell) {
        if (cell == null || cells == null) {
            return null;
        }

        Coordinate cellLocation = cell.getLocation();

        return (
            cellLocation == null ||
                cellLocation.getRow() < 0 ||
                cellLocation.getRow() > cells.length ||
                cellLocation.getColumn() < 0 ||
                cellLocation.getColumn() > cells[cellLocation.getRow()].length
        ) ? null : cells[cellLocation.getRow()][cellLocation.getColumn()];

    }

    @Override
    public SolverAdapter clone() {
        return new DFSSolverAdapter();
    }
}
