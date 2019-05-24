package Maze.Solver.BFS;

import Helpers.Coordinate;
import Helpers.SuccessCloneable;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Helpers.Utils;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

public class DistancesResults implements SuccessCloneable<DistancesResults> {
    private Coordinate coordinate;

    /**
     * Map of that the key is the row number and the key for the map in the value is column
     */
    private AbstractMap<Integer, AbstractMap<Integer, BFSSolverAdapter.SearchResult>> distances = new ConcurrentHashMap<>();

    public DistancesResults() {
    }

    public DistancesResults(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    private DistancesResults(Coordinate coordinate, AbstractMap<Integer, AbstractMap<Integer, BFSSolverAdapter.SearchResult>> distances) {
        this.coordinate = coordinate;
        this.distances = distances;
    }

    public BFSSolverAdapter.SearchResult getSearchResultForCoordinates(Coordinate position) {
        ObjectAssertion.requireNonNull(position, "position can't be null");

        if (!hasDistances()) {
            // TODO - Throw not found exception
            return null;
        }

        AbstractMap<Integer, BFSSolverAdapter.SearchResult> searchResultForRow = this.distances.get(position.getRow());

        if (searchResultForRow == null) {
            // TODO - Throw not found exception
            return null;
        }

        // TODO - If not found throw not found exception
        return searchResultForRow.get(position.getColumn());
    }

    public boolean hasDistanceForLocation(Coordinate coordinate) {
        return coordinate != null && coordinate.equals(this.coordinate) && this.hasDistances();
    }

    public void addDistanceRecord(Coordinate from, BFSSolverAdapter.SearchResult searchResult) {
        AbstractMap<Integer, BFSSolverAdapter.SearchResult> columnMap = new ConcurrentHashMap<>();
        columnMap.put(from.getColumn(), searchResult);

        AbstractMap<Integer, BFSSolverAdapter.SearchResult> currentColumnDistances = this.distances.putIfAbsent(from.getRow(), columnMap);

        if (currentColumnDistances != null) {
            currentColumnDistances.put(from.getColumn(), searchResult);
        }
    }

    public void reinitialize(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.cleanDistances();
    }


    public void cleanDistances() {
        this.distances.clear();
    }

    public boolean hasDistances() {
        return !distances.isEmpty();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void reversePaths() {
        distances.forEach(((row, colResults) -> colResults.forEach((col, searchResult) -> searchResult.reverseList())));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DistancesResults clone() {
        return new DistancesResults(Utils.clone(coordinate), cloneDistances());
    }

    private AbstractMap<Integer, AbstractMap<Integer, BFSSolverAdapter.SearchResult>> cloneDistances() {
        return Utils.deepCloneMap(this.distances, key -> key, map -> Utils.deepCloneMap(map, key -> key, BFSSolverAdapter.SearchResult::clone));
    }
}
