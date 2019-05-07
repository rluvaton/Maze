package Maze.Candy;

/**
 * Time Candy
 * @description Candy that can add / subtract time
 *
 * @implNote  candyStrength is in milliseconds
 */
public class TimeCandy extends Candy
{
    /**
     * Constructor
     * @param candyStrength How much in milliseconds to add / subtract
     * @param timeToLive Time until Expire
     */
    public TimeCandy(int candyStrength, int timeToLive) {
        super(CandyPowerType.Time, candyStrength, timeToLive);
    }

    public TimeCandy(int candyStrength) {
        super(CandyPowerType.Time, candyStrength);
    }

    @Override
    public String getColor() {
        return "#6761A8";
    }

    public static class Builder extends Candy.Builder {

        public static Builder create() {
            return new Builder();
        }

        private Builder() {
            super();
        }

        @Override
        public TimeCandy build() {
            return new TimeCandy(candyStrength, timeToLive);
        }
    }
}
