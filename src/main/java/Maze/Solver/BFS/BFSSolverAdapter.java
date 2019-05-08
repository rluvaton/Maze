package Maze.Solver.BFS;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        return solveMaze(maze.getMazeData(), start, end, endingCell);
    }

    @Override
    public Direction[] solveMaze(Cell[][] maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception {
        Cell endingCell = maze[end.getRow()][end.getColumn()];

        return solveMaze(maze, start, end, endingCell);
    }

    private Direction[] solveMaze(Cell[][] mazeData, Coordinate start, Coordinate end, Cell endingCell) {

        if(endingCell == null) {
            throw new IllegalArgumentException("Ending cell can't be null " + end.toString());
        }

        // Checking if start isn't equal to ending coordination to save redundant distance calculation
        if (this.endResults == null || (!end.equals(endingCoordination) && !start.equals(endingCoordination))) {
            this.endingCoordination = end;
            this.endResults = this.getAllDistancesAndPathFromEverywhereToEnd(mazeData, endingCell);
        }

        // This is for
        if(endingCoordination.equals(start)) {
            Coordinate swapHelper = end;
            end = start;
            start = swapHelper;
        }

        SearchResult result = this.endResults.get(start.getRow()).get(start.getColumn());

        return result.path.toArray(new Direction[0]);
    }

    private HashMap<Integer, HashMap<Integer, SearchResult>> getAllDistancesAndPathFromEverywhereToEnd(Cell[][] mazeData, Cell end) {
        LinkedList<Cell> queue = new LinkedList<>();
        queue.add(end);

        HashMap<Integer, HashMap<Integer, SearchResult>> distances = new HashMap<>();

        Set<Map.Entry<Direction, Cell>> neighbors;

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
            neighbors = currentCell.getNeighbors().entrySet().stream()
                    .map(directionNeighborCellEntry -> {
                        Cell.NeighborCell neighborCellEntryValue = directionNeighborCellEntry.getValue();
                        return new AbstractMap.SimpleEntry<Direction, Cell>(directionNeighborCellEntry.getKey(),
                                (neighborCellEntryValue != null && neighborCellEntryValue.getCell() != null)
                                        ? neighborCellEntryValue.getCell() : null
                        );
                    })
                    .filter(directionCellEntry -> directionCellEntry.getValue() != null).collect(Collectors.toSet());

            for (Map.Entry<Direction, Cell> neighbor : neighbors) {
                neighborCell = neighbor.getValue();
                neighborDirection = neighbor.getKey();
                Coordinate neighborLocation = neighborCell.getLocation();

                neighborNodeSearchResult = distances.get(neighborLocation.getRow()).get(neighborLocation.getColumn());

                if (neighborNodeSearchResult.getDistance() == -1) {
                    neighborNodeSearchResult.setDistance(currentNodeSearchResult.getDistance() + 1);
                    neighborNodeSearchResult.path = new LinkedList<>(currentNodeSearchResult.path);
                    neighborNodeSearchResult.path.add(Direction.getOppositeDirection(neighborDirection));

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
