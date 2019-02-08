package Maze.Solver.BFS;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;
import Maze.ELocationType;
import Maze.Maze;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

import static Helpers.Utils.DIRECTIONS;

public class BFSSolverAdapter extends SolverAdapter {
    /**
     * Solve Maze
     *
     * @param maze        Maze to solve
     * @param start       Starting location to solve from
     * @param end         Ending Location to solve from
     * @param withCandies Solve maze with candies
     * @return Direction of the path that solved
     * @throws Exception When the maze can't be solved
     */
    @Override
    public Direction[] solveMaze(Maze maze, Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, boolean withCandies) throws Exception {
        ArrayList<Direction> steps = new ArrayList<>();
        LinkedList<Tuple<Integer, Integer>> nextToVisit = new LinkedList<>();

        BFSCell[][] bfsCells = this.convertMazeCellsToBFSCells(maze.getMazeData());

        nextToVisit.add(start);

        Tuple<Integer, Integer> currentloc;

        while (!nextToVisit.isEmpty()) {
            currentloc = nextToVisit.remove();
            steps.remove(0);

            if (!maze.checkIfValidLocation(currentloc) || getBFSCellFromLocation(bfsCells, currentloc).isVisited()) {
                continue;
            }

            for (Map.Entry<Direction, Tuple<Integer, Integer>> entry : DIRECTIONS.entrySet()) {
                Direction direction = entry.getKey();

                steps.add(direction);

                // Check if is Exit
                if (maze.checkIfELocation(currentloc, direction, ELocationType.Exit) != null) {
                    return steps.toArray(new Direction[0]);
                }

                BFSCell cell = getBFSCellFromCell(bfsCells, maze.checkIfValidMoveCell(currentloc, direction));

                if (cell != null) {
                    cell.setVisited(true);

                    if(withCandies) {
                        Tuple<Integer, Integer> maybeNextLocation = cell.collectLocationCandyPortal();

                        if(maybeNextLocation != null) {
                            nextToVisit.add(maybeNextLocation);
                            continue;
                        }
                    }
                }

                nextToVisit.add(Utils.Instance.getNextLocation(currentloc, entry.getValue()));
            }
        }

        return new Direction[0];
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
    public Direction[] solveMaze(Maze maze, Tuple<Integer, Integer> start, Tuple<Integer, Integer> end, boolean withCandies, Function<Direction, Void> stepCallback) throws Exception {
        return new Direction[0];
    }

    private BFSCell[][] convertMazeCellsToBFSCells(Cell[][] cells) {
        return Arrays.stream(cells).map(cellsRow -> Arrays.stream(cellsRow).map(cell -> (BFSCell) cell).toArray(BFSCell[]::new)).toArray(BFSCell[][]::new);
    }

    private BFSCell getBFSCellFromCell(BFSCell[][] cells, Cell cell) {
        if (cell == null || cells == null) {
            return null;
        }

        return getBFSCellFromLocation(cells, cell.getLocation());
    }

    private BFSCell getBFSCellFromLocation(BFSCell[][] cells, Tuple<Integer, Integer> cellLocation) {
        return (
            cellLocation == null ||
                cellLocation.item1 < 0 ||
                cellLocation.item1 > cells.length ||
                cellLocation.item2 < 0 ||
                cellLocation.item2 > cells[cellLocation.item1].length
        ) ? null : cells[cellLocation.item1][cellLocation.item2];
    }
}
