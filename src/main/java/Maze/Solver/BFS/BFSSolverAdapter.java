package Maze.Solver.BFS;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.SuccessCloneable;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BFSSolverAdapter extends SolverAdapter {

    private DistancesResults endResults = new DistancesResults();

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
    public List<Direction> solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception {
        Cell endingCell = maze.getCell(end);

        return solveMaze(maze.getMazeData(), start, end, endingCell);
    }

    private List<Direction> solveMaze(Cell[][] mazeData, Coordinate start, Coordinate end, Cell endingCell) {
        ObjectAssertion.requireNonNull(endResults, "End results should never be null");

        if (endingCell == null) {
            throw new IllegalArgumentException("Ending cell can't be null " + end.toString());
        }

        // Checking if start isn't equal to ending coordination to save redundant distance calculation
        if (!this.endResults.hasDistanceForLocation(end) && !this.endResults.hasDistanceForLocation(start)) {
            this.endResults.reinitialize(end);
            this.getDistanceResultToEnd(mazeData, endingCell);
        }

        if (this.endResults.hasDistanceForLocation(start)) {
            Coordinate swapHelper = end;
            end = start;
            start = swapHelper;
        }

        SearchResult result = this.endResults.getSearchResultForCoordinates(start);

        return result.path;
    }

    private void getDistanceResultToEnd(Cell[][] mazeData, Cell end) {
        ObjectAssertion.requireNonNull(mazeData, "`mazeData` can't be null");
        ObjectAssertion.requireNonNull(end, "`end` can't be null");

        LinkedList<Cell> queue = new LinkedList<>();
        queue.add(end);

        initializeAllDirectionResultsForMaze(mazeData);

        Set<Map.Entry<Direction, Cell>> neighbors;

        Coordinate currentCellLocation = end.getLocation();
        endResults.getSearchResultForCoordinates(currentCellLocation).setDistance(1);

        SearchResult currentNodeSearchResult;

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();
            currentCellLocation = currentCell.getLocation();

            currentNodeSearchResult = endResults.getSearchResultForCoordinates(currentCellLocation);

            neighbors = getNonNullCellNeighbors(currentCell);

            updateNeighborsSearchResults(queue, neighbors, currentNodeSearchResult);
        }

        endResults.reversePaths();
    }

    private void updateNeighborsSearchResults(LinkedList<Cell> queue, Set<Map.Entry<Direction, Cell>> neighbors, SearchResult currentNodeSearchResult) {
        Cell neighborCell;
        Direction neighborDirection;
        SearchResult neighborNodeSearchResult;

        for (Map.Entry<Direction, Cell> neighbor : neighbors) {
            neighborCell = neighbor.getValue();
            neighborDirection = neighbor.getKey();
            Coordinate neighborLocation = neighborCell.getLocation();

            neighborNodeSearchResult = endResults.getSearchResultForCoordinates(neighborLocation);

            if(updateNeighborNodeSearchResult(currentNodeSearchResult, neighborNodeSearchResult, neighborDirection)) {
                queue.add(neighborCell);
            }
        }
    }

    private boolean updateNeighborNodeSearchResult(SearchResult currentNodeSearchResult, SearchResult neighborNodeSearchResult, Direction neighborDirection) {
        if (neighborNodeSearchResult.getDistance() != -1) {
            return false;
        }

        neighborNodeSearchResult.setDistance(currentNodeSearchResult.getDistance() + 1);
        neighborNodeSearchResult.path = new LinkedList<>(currentNodeSearchResult.path);
        neighborNodeSearchResult.path.add(Direction.getOppositeDirection(neighborDirection));
        return true;
    }

    private Set<Map.Entry<Direction, Cell>> getNonNullCellNeighbors(Cell currentCell) {
        ObjectAssertion.requireNonNull(currentCell, "`currentCell` can't be null");

        return currentCell.getNeighbors().entrySet().stream()
                .map(directionNeighborCellEntry -> {
                    Cell.NeighborCell neighborCellEntryValue = directionNeighborCellEntry.getValue();

                    Cell cell = null;
                    if (neighborCellEntryValue != null) {
                        cell = neighborCellEntryValue.getCell();
                    }

                    return new AbstractMap.SimpleEntry<>(directionNeighborCellEntry.getKey(), cell);
                })
                .filter(directionCellEntry -> directionCellEntry.getValue() != null).collect(Collectors.toSet());
    }

    private void initializeAllDirectionResultsForMaze(Cell[][] mazeData) {
        ObjectAssertion.requireNonNull(mazeData, "`mazeData` can't be null");

        for (Cell[] row : mazeData) {
            for (Cell cell : row) {
                endResults.addDistanceRecord(cell.getLocation(), new SearchResult());
            }
        }
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

    @Override
    public SolverAdapter clone() {
        BFSSolverAdapter cloned = new BFSSolverAdapter();

        cloned.endResults = Utils.clone(endResults);

        return cloned;
    }

    public class SearchResult implements SuccessCloneable<SearchResult> {
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

        public void reverseList() {
            path = (LinkedList<Direction>) (Utils.Instance.reverseList(path));
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public SearchResult clone() {
            return new SearchResult(distance, (LinkedList<Direction>) path.clone());
        }
    }
}
