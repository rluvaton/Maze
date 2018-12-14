package Maze.Candy;

/**
 * Candy in cell
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

    // region Constructors

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

    // endregion

    // region Getter & Setter

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public int getCandyStrength() {
        return CandyStrength;
    }

    public void setCandyStrength(int candyStrength) {
        CandyStrength = candyStrength;
    }

    public CandyPowerType getType() {
        return type;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    // endregion
}
