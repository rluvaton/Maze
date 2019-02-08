package Helpers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<Direction, Tuple<Integer, Integer>> DIRECTIONS = Utils.createDirectionsMap();

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

    /**
     * Get next cell of of direction and current location
     *
     * @param loc           Location
     * @param nextDirection The direction to go
     * @return Returns new Tuple of the next cell location
     * @throws IllegalArgumentException in case of loc variable is null or nextDirection value is not recognized
     */
    public final Tuple<Integer, Integer> getNextCell(Tuple<Integer, Integer> loc, Direction nextDirection) {
        if (loc == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        Tuple<Integer, Integer> dirToAdd = DIRECTIONS.get(nextDirection);

        if (dirToAdd == null) {
            throw new IllegalArgumentException("Next direction didn't recognized");
        }

        return createLocTuple(loc.item1 + dirToAdd.item1, loc.item2 + dirToAdd.item2);
    }

    public final Tuple<Integer, Integer> getNextLocation(Tuple<Integer, Integer> loc, Tuple<Integer, Integer> dirToAdd) {
        if (loc == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        if (dirToAdd == null) {
            throw new IllegalArgumentException("Next direction didn't recognized");
        }

        return createLocTuple(loc.item1 + dirToAdd.item1, loc.item2 + dirToAdd.item2);
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
     * @return Returns The Tuple of items
     * @example Generate Location in Matrix
     * generateTuple(height, width)
     */
    public Tuple<Integer, Integer> generateTuple(int firstLimit, int secondLimit) {
        return new Tuple<>(getRandomNumber(firstLimit), getRandomNumber(secondLimit));
    }

    /**
     * Get Direction of the move
     *
     * @param from From location
     * @param to   To location
     * @return Direction of move
     * @throws IllegalArgumentException If from or to or there values are null
     */
    public Direction getDirection(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to) {
        if (from == null || from.item1 == null || from.item2 == null) {
            throw new IllegalArgumentException("from or it's values is null");
        }
        if (to == null || to.item1 == null || to.item2 == null) {
            throw new IllegalArgumentException("to or it's values is null");
        }

        return (from.item1 > to.item1 && from.item2.equals(to.item2)) ? Direction.BOTTOM :
            (from.item1 < to.item1 && from.item2.equals(to.item2)) ? Direction.TOP :
                (from.item1.equals(to.item1) && from.item2 > to.item2) ? Direction.LEFT :
                    (from.item1.equals(to.item1) && from.item2 < to.item2) ? Direction.RIGHT : null;
    }


    public static Map<Direction, Tuple<Integer, Integer>> createDirectionsMap() {
        HashMap<Direction, Tuple<Integer, Integer>> directions = new HashMap<>();

        directions.put(Direction.TOP, new Tuple<>(-1, 0));
        directions.put(Direction.RIGHT, new Tuple<>(0, 1));
        directions.put(Direction.BOTTOM, new Tuple<>(1, 0));
        directions.put(Direction.LEFT, new Tuple<>(0, -1));

        return directions;
    }
}

