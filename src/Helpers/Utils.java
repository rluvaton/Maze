package Helpers;

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

    public static Map<Direction, Coordinate> DIRECTIONS = Utils.createDirectionsMap();
    public static Map<Direction, Direction> OPPOSITE_DIRECTIONS = Utils.createOppositeDirectionsMap();

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
    public final Coordinate getNextLocation(Coordinate loc, Direction nextDirection) {
        if (loc == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        return this.getNextLocation(loc, DIRECTIONS.get(nextDirection));
    }

    public final Coordinate getNextLocation(Coordinate loc, Coordinate dirToAdd) {
        if (loc == null) {
            throw new IllegalArgumentException("Location can't be null");
        }

        return this.getNextLocation(loc.getRow(), loc.getColumn(), dirToAdd);
    }

    public final Coordinate getNextLocation(int currRow, int currCol, Coordinate dirToAdd) {
        if (dirToAdd == null) {
            throw new IllegalArgumentException("Next direction didn't recognized");
        }

        return new Coordinate(currRow + dirToAdd.getRow(), currCol + dirToAdd.getColumn());
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

    public <T> List<T> reverseList(List<T> list) {
        LinkedList<T> reversed = new LinkedList<>(list);
        Collections.reverse(reversed);
        return reversed;
    }


    public static Map<Direction, Coordinate> createDirectionsMap() {
        HashMap<Direction, Coordinate> directions = new HashMap<>();

        directions.put(Direction.TOP, new Coordinate(-1, 0));
        directions.put(Direction.RIGHT, new Coordinate(0, 1));
        directions.put(Direction.BOTTOM, new Coordinate(1, 0));
        directions.put(Direction.LEFT, new Coordinate(0, -1));

        return directions;
    }

    private static Map<Direction, Direction> createOppositeDirectionsMap() {
        HashMap<Direction, Direction> directions = new HashMap<>();

        directions.put(Direction.TOP, Direction.BOTTOM);
        directions.put(Direction.RIGHT, Direction.LEFT);
        directions.put(Direction.BOTTOM, Direction.TOP);
        directions.put(Direction.LEFT, Direction.RIGHT);

        return directions;
    }

    public Direction getOppositeDirection(Direction direction) {
        return Utils.OPPOSITE_DIRECTIONS.get(direction);
    }
}

