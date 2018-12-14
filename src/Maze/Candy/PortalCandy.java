package Maze.Candy;

import Helpers.Tuple;
import Maze.Cell;

/**
 * Portal Candy
 * @description When enter to cell the candy portal the player to a new place
 */
public class PortalCandy extends Candy
{
    /**
     * Is the Portal work 2 way
     *
     * @example twoWayPortal is true
     * I enter a portal and I can return to the same place that I enter the portal before
     */
    private boolean twoWayPortal = false;

    /**
     * Where the portal is moving you
     */
    private Tuple<Integer, Integer> location;

    // region Constructors

    public PortalCandy(boolean isGood, Tuple<Integer, Integer> location) {
        super(isGood, CandyPowerType.Location);
        this.location = location;
    }

    public PortalCandy(boolean isGood, Tuple<Integer, Integer> location, boolean twoWayPortal) {
        super(isGood, CandyPowerType.Location);
        this.location = location;
        this.twoWayPortal = twoWayPortal;
    }

    public PortalCandy(boolean isGood, Tuple<Integer, Integer> location, Cell otherCell, Tuple<Integer, Integer> myLocation) {
        super(isGood, CandyPowerType.Location);
        this.location = location;

        assert otherCell != null;
        assert myLocation != null;
        assert myLocation.item1 >= 0;
        assert myLocation.item2 >= 0;

        this.twoWayPortal = true;

        if (otherCell.getCandies().stream().noneMatch(candy ->
                candy instanceof PortalCandy &&
                        candy.isGood == isGood &&
                        ((PortalCandy) candy).twoWayPortal &&
                        ((PortalCandy) candy).location != null &&
                        (((PortalCandy) candy).location == myLocation ||
                                (((PortalCandy) candy).location.item1.equals(myLocation.item1) && ((PortalCandy) candy).location.item2.equals(myLocation.item2)))))
        {
            otherCell.addCandy(new PortalCandy(isGood, myLocation, true));
        }
    }

    public PortalCandy(boolean isGood, int timeToLive, Tuple<Integer, Integer> location, boolean twoWayPortal) {
        super(isGood, CandyPowerType.Location, timeToLive);
        this.location = location;
        this.twoWayPortal = twoWayPortal;
    }

    public PortalCandy(boolean isGood, int timeToLive, Tuple<Integer, Integer> location, Cell otherCell, Tuple<Integer, Integer> myLocation) {
        super(isGood, CandyPowerType.Location, timeToLive);
        this.location = location;

        assert otherCell != null;
        assert myLocation != null;
        assert myLocation.item1 >= 0;
        assert myLocation.item2 >= 0;

        this.twoWayPortal = true;

        if (otherCell.getCandies().stream().noneMatch(candy ->
                candy instanceof PortalCandy &&
                        candy.isGood == isGood &&
                        candy.timeToLive == timeToLive &&
                        ((PortalCandy) candy).twoWayPortal &&
                        ((PortalCandy) candy).location != null &&
                        (((PortalCandy) candy).location == myLocation ||
                                (((PortalCandy) candy).location.item1.equals(myLocation.item1) && ((PortalCandy) candy).location.item2.equals(myLocation.item2)))))
        {
            otherCell.addCandy(new PortalCandy(isGood, timeToLive, myLocation, true));
        }
    }

    public PortalCandy(boolean isGood, int candyStrength, boolean twoWayPortal, Tuple<Integer, Integer> location) {
        super(isGood, candyStrength, CandyPowerType.Location);
        this.twoWayPortal = twoWayPortal;
        this.location = location;
    }

    public PortalCandy(boolean isGood, int candyStrength, int timeToLive, boolean twoWayPortal, Tuple<Integer, Integer> location) {
        super(isGood, candyStrength, CandyPowerType.Location, timeToLive);
        this.twoWayPortal = twoWayPortal;
        this.location = location;
    }

    // endregion

    // region Getter & Setter

    public boolean isTwoWayPortal() {
        return twoWayPortal;
    }

    public void setTwoWayPortal(boolean twoWayPortal) {
        this.twoWayPortal = twoWayPortal;
    }

    public Tuple<Integer, Integer> getLocation() {
        return location;
    }

    public void setLocation(Tuple<Integer, Integer> location) {
        this.location = location;
    }

    // endregion
}
