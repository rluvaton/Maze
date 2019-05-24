package Maze.Candy;


import Helpers.Builder.IBuilder;

import java.util.Objects;

/**
 * Candy in cell
 */
public abstract class Candy {
    public static int WITHOUT_TIMEOUT = -1;

    /**
     * How much the candy help you
     * (Negetive number is bad points
     */
    protected int candyStrength = 0;

    /**
     * What the candy change
     */
    protected CandyPowerType type;

    /**
     * Time in milliseconds of candy to live, until it's expired and disappear
     * -1 means no time
     */
    protected int timeToLive = Candy.WITHOUT_TIMEOUT;

    // region Constructors


    public Candy(CandyPowerType type, int candyStrength) {
        this.type = type;
        this.candyStrength = candyStrength;
        this.timeToLive = Candy.WITHOUT_TIMEOUT;
    }

    public Candy(CandyPowerType type, int candyStrength, int timeToLive) {
        this.type = type;
        this.candyStrength = candyStrength;
        this.timeToLive = timeToLive;
    }

    // endregion

    public String getColor() {
        return "black";
    }

    // region Getter & Setter

    public int getCandyStrength() {
        return candyStrength;
    }

    public void setCandyStrength(int candyStrength) {
        this.candyStrength = candyStrength;
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

    public boolean equals(Object candy) {
        return candy instanceof Candy &&
                this.timeToLive == ((Candy) candy).timeToLive &&
                this.type == ((Candy) candy).type &&
                this.candyStrength == ((Candy) candy).candyStrength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(candyStrength, type, timeToLive);
    }

    // region Builder

    public abstract static class Builder implements IBuilder<Candy> {

        protected CandyPowerType type;
        protected int candyStrength = 0;
        protected int timeToLive = -1;

        public static Candy.Builder createForType(CandyPowerType type) {
            assert type != null;

            switch (type) {
                case Time:
                    return TimeCandy.Builder.create();
                case Points:
                    return PointsCandy.Builder.create();
                case Location:
                    return PortalCandy.Builder.create();
                default:
                    throw new IllegalStateException("Unexpected CandyPowerType value: " + type);
            }
        }

        protected Builder() {
        }

        // region Setter Methods

        public Builder setCandyStrength(int candyStrength) {
            this.candyStrength = candyStrength;
            return this;
        }

        public Builder setTimeToLive(int timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }

        // endregion

        public abstract Candy build();

    }
    // endregion
}
