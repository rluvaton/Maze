package Helpers;

import Maze.Direction;

import java.util.Arrays;
import java.util.Random;
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
     * @return The random state (true or false
     */
    public final boolean getRandomState() {
        return _random.nextInt(2) == 1;
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

    /**
     * Get next cell of of direction and current location
     * @param loc Location
     * @param nextDirection The direction to go
     * @return Returns new Tuple of the next cell location
     * @throws IllegalArgumentException in case of loc variable is null or nextDirection value is not recognized
     */
    public final Tuple<Integer, Integer> getNextCell(Tuple<Integer, Integer> loc, Direction nextDirection) {
        if(loc == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        int row = loc.item1,
                col = loc.item2;
        switch (nextDirection) {
            case TOP:
                row -= 1;
                break;
            case BOTTOM:
                row += 1;
                break;
            case RIGHT:
                col += 1;
                break;
            case LEFT:
                col -= 1;
                break;
            default:
                throw new IllegalArgumentException("Next direction didn't recognized");
        }

        return createLocTuple(row, col);
    }

    /**
     * Create Location Tuple
     *
     * @param row Row
     * @param col Column
     * @return Return the tuple of the location
     */
    public final Tuple<Integer, Integer> createLocTuple(int row, int col) {
        return new Tuple<>(row, col);
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
    public final <T> boolean inBounds(Tuple<Integer, Integer> loc, int height, int width) {
        return loc.item1 >= 0 && loc.item1 < height && loc.item2 >= 0 && loc.item2 < width;
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
        return (row == 0 && direction == Direction.TOP) ||
                (row == height - 1 && direction == Direction.BOTTOM) ||
                (col == 0 && direction == Direction.LEFT) ||
                (col == width - 1 && direction == Direction.RIGHT);
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
     * Compare tuples
     * @param t1 Tuple 1
     * @param t2 Tuple 2
     * @param <X> First type of the tuple
     * @param <Y> Second type of the tuple
     * @return Returns if both tuples are equal in values or as tuples
     */
    public <X, Y> boolean compareTuples(Tuple<X, Y> t1, Tuple<X, Y> t2) {
        return t1 == t2 || (t1.item1 == t2.item1 && t1.item2 == t2.item2);
    }
}

