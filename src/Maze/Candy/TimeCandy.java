package Maze.Candy;

/**
 * Time Candy
 * @description Candy that can add / subtract time
 *
 * @implNote  candyStrength is in milliseconds
 */
public class TimeCandy extends Candy
{
    public TimeCandy(boolean isGood, int candyStrength) {
        super(isGood, (isGood ? 1 : -1) * Math.abs(candyStrength), CandyPowerType.Time);
    }

    public TimeCandy(int candyStrength) {
        super(candyStrength >= 0, candyStrength, CandyPowerType.Time);
    }

    public TimeCandy(boolean isGood, int candyStrength, int timeToLive) {
        super(isGood, (isGood ? 1 : -1) * Math.abs(candyStrength), CandyPowerType.Time, timeToLive);
    }
}
