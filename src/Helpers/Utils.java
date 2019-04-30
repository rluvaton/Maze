package Helpers;

import Maze.Cell;

import java.util.*;
import java.util.stream.Stream;

public class Utils {
    /**
     * Helpers.Helpers Instance
     */
    public static final Utils Instance = new Utils();

    /**
     * Random Instance for random numbers one after another
     */
    private final Random _random = new Random();

    /**
     * Get Random State
     *
     * @return The random state (true or false)
     */
    public final boolean getRandomState() {
        return _random.nextBoolean();
    }

    /**
     * Get Random Number
     *
     * @param from  From Number (Included)
     * @param until Until Number (Not included)
     * @return The random number
     */
    public final int getRandomNumber(int from, int until) {
        return _random.nextInt(until) + from;
    }

    /**
     * Get Random Number from 0 to the until variable
     *
     * @param until Until Number (Not included)
     * @return The random number
     */
    public final int getRandomNumber(int until) {
        return _random.nextInt(until);
    }

    public final Coordinate moveCoordinatesToDirection(Coordinate pos, Direction dir) {
        assert pos != null && dir != null;
        return this.moveCoordinatesToDirection(pos.getRow(), pos.getColumn(), dir);
    }

    public final Coordinate moveCoordinatesToDirection(int row, int col, Direction dir) {
        assert dir != null;

        Coordinate directionMove = dir.getValue();

        return new Coordinate(row + directionMove.getRow(), col + directionMove.getColumn());
    }

    /**
     * Check if index is in the boundaries of the vector
     *
     * @param index Index to check
     * @param arr   Array to check
     *              <typeparam name="T">Array Type</typeparam>
     * @return Returns if the index is in the vector boundaries
     */
    public final <T> boolean inBounds(int index, T[] arr) {
        return (index >= 0) && (index < arr.length);
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param row Row to check
     * @param col Col to check
     * @param arr Array to check
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(int row, int col, T[][] arr) {
        return col >= 0 && col < arr.length && row >= 0 && row < arr[0].length;
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param row    Row to check
     * @param col    Col to check
     * @param height Height of Matrix
     * @param width  Width of Matrix
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(int row, int col, int height, int width) {
        return col >= 0 && col < height && row >= 0 && row < width;
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param loc    Tuple of the row and the col in the matrix
     * @param height Height of Matrix
     * @param width  Width of Matrix
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(Coordinate loc, int height, int width) {
        return loc.getRow() >= 0 && loc.getRow() < height && loc.getColumn() >= 0 && loc.getColumn() < width;
    }

    /**
     * Check if row and column in limit
     *
     * @param row       row to check
     * @param col       column to check
     * @param direction direction to check
     * @param width     width of mat
     * @param height    height of mat
     * @return Returns if in limit
     */
    public final boolean inLimits(int row, int col, Direction direction, int width, int height) {
        return (row == 0 && direction == Direction.TOP) || (row == height - 1 && direction == Direction.BOTTOM) ||
                (col == 0 && direction == Direction.LEFT) || (col == width - 1 && direction == Direction.RIGHT);
    }


    /**
     * Convert Array to stream
     *
     * @param arr Array to convert
     * @param <T> Type of the array
     * @return Returns the stream of the array
     */
    public <T> Stream<T> convertArrayToStream(T[] arr) {
        return Arrays.stream(arr);
    }

    /**
     * Convert Matrix to stream of stream
     *
     * @param mat Matrix to convert
     * @param <T> type of the matrix
     * @return Returns the stream of stream of the matrix
     */
    public <T> Stream<Stream<T>> convertMatrixToStream(T[][] mat) {
        return Arrays.stream(mat).map(Arrays::stream);
    }

    /**
     * Generate Tuple
     *
     * @param firstLimit  First item limit
     * @param secondLimit Second item limit
     * @return Returns The Coordinate
     * @example Generate Location in Matrix
     * generateCoordinate(height, width)
     */
    public Coordinate generateCoordinate(int firstLimit, int secondLimit) {
        return new Coordinate(getRandomNumber(firstLimit), getRandomNumber(secondLimit));
    }

    public Direction getDirectionOfMove(Coordinate from, Coordinate to) {
        assert from != null && to != null;

        Direction[] allDirections = Direction.values();
        Coordinate directionCoordinates = new Coordinate(from.getRow() - to.getRow(), from.getColumn() - to.getColumn());

        for (Direction direction : allDirections) {
            if (directionCoordinates.equals(direction.getValue())) {
                return direction;
            }
        }

        throw new IllegalArgumentException("from and not near each other");
    }

    public <T> List<T> reverseList(List<T> list) {
        LinkedList<T> reversed = new LinkedList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }

    public <K, V> Map<K, V> cloneMap(Map<K, V> source, Map<K, V> cloneTo) {
        source.forEach(cloneTo::put);
        return cloneTo;
    }
}

