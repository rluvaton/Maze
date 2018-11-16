package Maze.Candy;

/**
 * Candy Object for candy in cell
 */
public abstract class Candy
{
    /**
     * This mean that if the candy gonna help you or reduce points
     */
    protected boolean isGood;

    /**
     * How much the candy help you
     */
    protected int CandyStrength = 0;

    /**
     * What the candy change
     */
    protected CandyPowerType type;

    /**
     * Time of candy to live
     * -1 means no time
     */
    protected int timeToLive = -1;


    public Candy(boolean isGood, CandyPowerType type) {
        this.isGood = isGood;
        this.type = type;
    }

    public Candy(boolean isGood, CandyPowerType type, int timeToLive) {
        this.isGood = isGood;
        this.type = type;
        this.timeToLive = timeToLive;
    }

    public Candy(boolean isGood, int candyStrength, CandyPowerType type) {
        this.isGood = isGood;
        CandyStrength = candyStrength;
        this.type = type;
    }

    public Candy(boolean isGood, int candyStrength, CandyPowerType type, int timeToLive) {
        this.isGood = isGood;
        CandyStrength = candyStrength;
        this.type = type;
        this.timeToLive = timeToLive;
    }
}
