package Maze.Candy;

import Helpers.Coordinate;
import Helpers.NoArgsCallbackFunction;
import Helpers.Tuple;
import Maze.Cell;

/**
 * Portal Candy
 *
 * @description When enter to cell the candy portal the player to a new place
 */
public class PortalCandy extends Candy {
    /**
     * Is the Portal work 2 way
     *
     * @example twoWayPortal is true
     * I enter a portal and I can return to the same place that I enter the portal before
     */
    private boolean twoWayPortal = true;

    /**
     * Where the portal is moving you
     */
    private Coordinate location;

    // region Constructors

    /**
     * Portal Candy Constructor
     *
     * @param location location of where the candy is teleporting you
     *
     * Private function because we don't want for now 1 Way portal candy
     *
     * @see #PortalCandy(Coordinate, Cell, Coordinate) for creating portal candy
     */
    private PortalCandy(Coordinate location) {
        super(CandyPowerType.Location);
        this.location = location;
    }

    public PortalCandy(Coordinate location, Cell otherCell, Coordinate myLocation) {
        super(CandyPowerType.Location);
        this.location = location;

        this.addOtherCellTeleportCandy(otherCell, myLocation, () -> new PortalCandy(myLocation));
    }

    /**
     * Portal Candy Constructor
     *
     * @param location   location of where the candy is teleporting you
     * @param timeToLive Time until the candy expired
     *
     * Private function because we don't want for now 1 Way portal candy
     *
     * @see #PortalCandy(int, Coordinate, Cell, Coordinate) for creating portal candy
     */
    private PortalCandy(Coordinate location, int timeToLive) {
        super(CandyPowerType.Location, timeToLive);
        this.location = location;
    }

    public PortalCandy(int timeToLive, Coordinate location, Cell otherCell, Coordinate myLocation) {
        super(CandyPowerType.Location, timeToLive);
        this.location = location;

        this.addOtherCellTeleportCandy(otherCell, myLocation, () -> new PortalCandy(myLocation, timeToLive));
    }

    // endregion

    private void addOtherCellTeleportCandy(Cell cell, Coordinate myLocation, NoArgsCallbackFunction<PortalCandy> createCandy) {

        if (cell == null) {
            throw new IllegalArgumentException("cell can't be null");
        }

        if (myLocation == null) {
            throw new IllegalArgumentException("myLocation can't be null");
        }

        if (myLocation.getRow() < 0) {
            throw new IllegalArgumentException("myLocation row can't be negative");
        }

        if (myLocation.getColumn() < 0) {
            throw new IllegalArgumentException("myLocation column can't be negative");
        }

        if (cell.getCandies().stream().noneMatch(candy ->
                candy instanceof PortalCandy &&
                        candy.isGood == isGood &&
                        candy.timeToLive == timeToLive &&
                        ((PortalCandy) candy).twoWayPortal &&
                        ((PortalCandy) candy).location != null &&
                        (((PortalCandy) candy).location == myLocation ||
                                (((PortalCandy) candy).location.equals(myLocation))))) {
            cell.addCandy(createCandy.run());
        }
    }

    @Override
    public String getColor() {
        return "#009B72";
    }

    // region Getter & Setter

    public boolean isTwoWayPortal() {
        return twoWayPortal;
    }

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    // endregion
}
