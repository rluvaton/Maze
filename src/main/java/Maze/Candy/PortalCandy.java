package Maze.Candy;

import Helpers.Coordinate;
import Maze.Cell;

import java.util.Objects;

/**
 * Portal Candy
 *
 * @description When enter to cell the candy portal the player to a new place
 */
public class PortalCandy extends Candy {

    /**
     * Where the portal is moving you
     */
    private Coordinate location;

    // region Constructors

    public PortalCandy(Coordinate location) {
        super(CandyPowerType.Location, 0);
        this.location = location;
    }

    public PortalCandy(int timeToLive, Coordinate location) {
        this(0, timeToLive, location);
    }

    public PortalCandy(int candyStrength, int timeToLive, Coordinate location) {
        super(CandyPowerType.Location, candyStrength, timeToLive);
        this.location = location;
    }

    // endregion

    private boolean equalsWithCustomLocation(Candy candy) {
        return this.equalsWithoutLocation(candy) &&
                this.location != null &&
                this.location.equals(((PortalCandy) candy).location);
    }

    @Override
    public String getColor() {
        return "#009B72";
    }

    // region Getter & Setter

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    // endregion

    public boolean equalsWithoutLocation(Object candy) {
        return super.equals(candy) && candy instanceof PortalCandy;
    }

    @Override
    public boolean equals(Object candy) {
        return super.equals(candy) &&
                candy instanceof PortalCandy &&
                Coordinate.equals(this.location, ((PortalCandy) candy).location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }

    // region Builder

    public static class Builder extends Candy.Builder {
        /**
         * Current Candy location
         * Used for create the candy in the other side cell
         */
        private Coordinate myLocation;
        private Cell otherSideCell;

        /**
         * Where the candy will portal
         */
        private Coordinate otherSideLocation;

        public static Builder create() {
            return new Builder();
        }

        private Builder() {
            super();

            this.type = CandyPowerType.Location;
        }

        // region Setters

        public Builder setMyLocation(Coordinate myLocation) {
            this.myLocation = myLocation;
            return this;
        }

        public Builder setOtherSideCell(Cell otherSideCell) {
            this.otherSideCell = otherSideCell;
            return this;
        }

        public Builder setOtherSideLocation(Coordinate otherSideLocation) {
            this.otherSideLocation = otherSideLocation;
            return this;
        }

        // endregion

        @Override
        public PortalCandy build() {
            PortalCandy thisCandy = new PortalCandy(candyStrength, timeToLive, this.otherSideLocation);

            if (otherSideCell != null && this.myLocation != null) {
                this.addOtherCellTeleportCandy(thisCandy);
            }

            return thisCandy;
        }


        private void addOtherCellTeleportCandy(PortalCandy currentCandy) {
            assert otherSideCell != null && this.myLocation != null;

            if (!isThereAlreadyPortalCandyToMyLocation(currentCandy)) {
                otherSideCell.addCandy(cloneBuilderToOtherSideCandy());
            }
        }

        private boolean isThereAlreadyPortalCandyToMyLocation(PortalCandy currentCandy) {
            return otherSideCell
                    .getCandies()
                    .stream()
                    .anyMatch(candy ->
                            candy.getType() == currentCandy.getType() &&
                                    candy instanceof PortalCandy &&
                                    myLocation.equals(((PortalCandy) candy).getLocation()));
        }

        private PortalCandy cloneBuilderToOtherSideCandy() {
            return this.clone()
                    .setMyLocation(null)
                    .setOtherSideCell(null)
                    .setOtherSideLocation(myLocation)
                    .build();
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public Builder clone() {
            return (Builder) Builder.create()
                    .setOtherSideLocation(otherSideLocation == null ? null : otherSideLocation.clone())
                    .setOtherSideCell(otherSideCell)
                    .setMyLocation(myLocation == null ? null : myLocation.clone())
                    .setCandyStrength(candyStrength)
                    .setTimeToLive(timeToLive);
        }
    }

    // endregion
}
