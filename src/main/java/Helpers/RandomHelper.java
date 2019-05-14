package Helpers;

import java.util.Random;

public class RandomHelper {
    /**
     * Random Instance for random numbers one after another
     */
    private static final Random _random = new Random();

    /**
     * Get Random State
     *
     * @return The random state (true or false)
     */
    public static boolean getRandomState() {
        return _random.nextBoolean();
    }

    /**
     * Get Random Number
     *
     * @param from  From Number (Included)
     * @param until Until Number (Not included)
     * @return The random number
     */
    public static int getRandomNumber(int from, int until) {
        return _random.nextInt(until) + from;
    }

    /**
     * Get Random Number from 0 to the until variable
     *
     * @param until Until Number (Not included)
     * @return The random number
     */
    public static int getRandomNumber(int until) {
        return _random.nextInt(until);
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
    public static Coordinate generateCoordinate(int firstLimit, int secondLimit) {
        return new Coordinate(getRandomNumber(firstLimit), getRandomNumber(secondLimit));
    }

    public static <T> T generateItemFromArray(T[] items) {
        return items.length == 0 ? null : items[getRandomNumber(items.length)];
    }
}
