package Maze.Candy;

/**
 * Points Candy
 *
 * @description Add / Subtract points when collect this candy
 */
public class PointsCandy extends Candy {
    private final String CANDY_COLOR = "#F26430";

    public PointsCandy(int candyStrength) {
        super(CandyPowerType.Points, candyStrength);
    }

    public PointsCandy(int candyStrength, int timeToLive) {
        super(CandyPowerType.Points, candyStrength, timeToLive);
    }

    @Override
    public String getColor() {
        return CANDY_COLOR;
    }

    public static class Builder extends Candy.Builder {

        public static Builder create() {
            return new Builder();
        }

        @Override
        public PointsCandy build() {
            return new PointsCandy(this.candyStrength, this.timeToLive);
        }
    }
}
