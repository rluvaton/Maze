package Maze.Candy;

/**
 * Add / Subtract points when collect this candy
 */
public class PointsCandy extends Candy
{
    public PointsCandy(boolean isGood) {
        super(isGood, CandyPowerType.Points);
    }

    public PointsCandy(boolean isGood, int candyStrength) {
        super(isGood, candyStrength, CandyPowerType.Points);
    }

    public PointsCandy(boolean isGood, int candyStrength, int timeToLive) {
        super(isGood, candyStrength, CandyPowerType.Points, timeToLive);
    }
}
