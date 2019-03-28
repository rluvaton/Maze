package Maze.Solver.BFS;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BFSSolverAdapter extends SolverAdapter {

    private Coordinate endingCoordination = null;
    private HashMap<Integer, HashMap<Integer, SearchResult>> endResults = null;

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
    public Direction[] solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception {
        Cell endingCell = maze.getCell(end);

        if (this.endResults == null || !end.equals(endingCoordination)) {
            this.endingCoordination = end;
            this.endResults = this.getAllDistancesAndPathFromEverywhereToEnd(maze, endingCell);
        }

        SearchResult result = this.endResults.get(start.getRow()).get(start.getColumn());

        return result.path.toArray(new Direction[0]);
    }


    private HashMap<Integer, HashMap<Integer, SearchResult>> getAllDistancesAndPathFromEverywhereToEnd(Maze maze, Cell end) {
        LinkedList<Cell> queue = new LinkedList<>();
        queue.add(end);

        HashMap<Integer, HashMap<Integer, SearchResult>> distances = new HashMap<>();
        Cell[][] mazeData = maze.getMazeData();

        LinkedList<Tuple<Cell, Direction>> neighbors;

        for (Cell[] row : mazeData) {
            for (Cell cell : row) {
                Coordinate cellLocation = cell.getLocation();
                HashMap<Integer, SearchResult> columnsResults = distances.computeIfAbsent(cellLocation.getRow(), k -> new HashMap<>());

                columnsResults.put(cellLocation.getColumn(), new SearchResult());
            }
        }

        Coordinate cellLocation = end.getLocation();
        distances.get(cellLocation.getRow()).get(cellLocation.getColumn()).setDistance(1);


        SearchResult currentNodeSearchResult;
        SearchResult neighborNodeSearchResult;
        Cell neighborCell;
        Direction neighborDirection;

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();
            cellLocation = currentCell.getLocation();

            currentNodeSearchResult = distances.get(cellLocation.getRow()).get(cellLocation.getColumn());
            neighbors = currentCell.getNeighbors();

            for (Tuple<Cell, Direction> neighbor : neighbors) {
                neighborCell = neighbor.item1;
                neighborDirection = neighbor.item2;
                Coordinate neighborLocation = neighborCell.getLocation();

                neighborNodeSearchResult = distances.get(neighborLocation.getRow()).get(neighborLocation.getColumn());

                if (neighborNodeSearchResult.distance == -1) {
                    neighborNodeSearchResult.distance = currentNodeSearchResult.distance + 1;
                    neighborNodeSearchResult.path = new LinkedList<>(currentNodeSearchResult.path);
                    neighborNodeSearchResult.path.add(Utils.Instance.getOppositeDirection(neighborDirection));

                    queue.add(neighborCell);
                }
            }
        }

        distances.forEach(((row, colResults) -> colResults.forEach((col, searchResult) -> searchResult.path = (LinkedList<Direction>) (Utils.Instance.reverseList(searchResult.path)))));

        return distances;
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

    private BFSCell[][] convertMazeCellsToBFSCells(Cell[][] cells) {
        return Arrays.stream(cells).map(cellsRow -> Arrays.stream(cellsRow).map(cell -> (BFSCell) new BFSCell(cell)).toArray(BFSCell[]::new)).toArray(BFSCell[][]::new);
    }

    private BFSCell getBFSCellFromCell(BFSCell[][] cells, Cell cell) {
        if (cell == null || cells == null) {
            return null;
        }

        return getBFSCellFromLocation(cells, cell.getLocation());
    }

    private BFSCell getBFSCellFromLocation(BFSCell[][] cells, Coordinate cellLocation) {
        return (
                cellLocation == null ||
                        cellLocation.getRow() < 0 ||
                        cellLocation.getRow() > cells.length ||
                        cellLocation.getColumn() < 0 ||
                        cellLocation.getColumn() > cells[cellLocation.getRow()].length
        ) ? null : cells[cellLocation.getRow()][cellLocation.getColumn()];
    }


    public class SearchResult {
        private int distance;
        private LinkedList<Direction> path;

        public SearchResult() {
            this.distance = -1;
            this.path = new LinkedList<>();
        }

        public SearchResult(int distance, LinkedList<Direction> path) {
            this.distance = distance;
            this.path = path;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public LinkedList<Direction> getPath() {
            return path;
        }

        public void setPath(LinkedList<Direction> path) {
            this.path = path;
        }
    }
}
