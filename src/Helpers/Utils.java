package Helpers;

import Maze.Direction;
import java.util.Random;

public class Utils
{
    /**
     * Helpers.Helpers Instance
     */
    public static final Utils Instance = new Utils();

    /**
     * Random Instance for random numbers one after another
     */
    private final Random _random = new Random();

    /**
     * Get Random Number
     *
     * @param from  From Number (Included)
     * @param until Until Number (Not included)
     * @return The random number
     */
    public final int getRandomNumber(int from, int until)
    {
        return _random.nextInt(until) + from;
    }

    /**
     * Get Random Number from 0 to the until variable
     *
     * @param until Until Number (Not included)
     * @return The random number
     */
    public final int getRandomNumber(int until)
    {
        return _random.nextInt(until);
    }

    /**
     * Get Horizontal Direction
     *
     * @param dir Direction - the value of <see cref="MazeGenerator.Maze.Direction"/>
     * @return Where To Go
     */
    public final int getHorizontalDirection(Direction dir)
    {
        return dir.getValue() / 2;
    }

    /**
     * Get Vertical Direction
     *
     * @param dir Direction - the value of <see cref="MazeGenerator.Maze.Direction"/>
     * @return Where To Go
     */
    public final int getVerticalDirection(Direction dir)
    {
        return dir.getValue();
    }

    /**
     * Check if index is in the boundaries of the vector
     *
     * @param index Index to check
     * @param arr   Array to check
     *              <typeparam name="T">Array Type</typeparam>
     * @return Returns if the index is in the vector boundaries
     */
    public final <T> boolean inBounds(int index, T[] arr)
    {
        return (index >= 0) && (index < arr.length);
    }

    /**
     * Check if row and column are in the boundaries of the matrix
     *
     * @param row Row to check
     * @param col Col to check
     * @param arr Array to check
     *            <typeparam name="T">Array Type</typeparam>
     * @return Returns if the row and column are in the matrix boundaries
     */
    public final <T> boolean inBounds(int row, int col, T[][] arr)
    {
        return row >= 0 && row < arr.length && col >= 0 && col < arr[0].length;
    }

}
