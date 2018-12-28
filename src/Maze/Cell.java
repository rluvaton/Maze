package Maze;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Helpers.Direction;
import Helpers.Tuple;
import Maze.Candy.*;


/**
 * Cell in maze
 */
public class Cell {

    // region Variables

    /**
     * If have wall at the top or not
     * If can't move top then it will be true
     */
    private boolean topWall = true;

    /**
     * If have wall at the right or not
     * If can't move right then it will be true
     */
    private boolean rightWall = true;

    /**
     * If have wall at the bottom or not
     * If can't move bottom then it will be true
     */
    private boolean bottomWall = true;

    /**
     * If have wall at the left or not
     * If can't move left then it will be true
     */
    private boolean leftWall = true;

    /**
     * If the cell contain candy
     */
    private ArrayList<Candy> candies = new ArrayList<>();

    // endregion

    // region Constructors

    /**
     * Default Constructor
     */
    public Cell() {
    }

    /**
     * Create Cube with one open neighbor
     *
     * @param neighbor  Neighbor to add
     * @param direction Direction of where to add the cube
     * @throws IllegalArgumentException  Throw Error if neighbor is null
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public Cell(Cell neighbor, Direction direction) {
        this(neighbor, direction, false);
    }

    /**
     * Create Cube with one open neighbor
     *
     * @param neighbor  Neighbor to add
     * @param direction Direction of where to add the cube
     * @param force     If to force the change (can delete other cube direction)
     * @throws IllegalArgumentException  Throw Error if neighbor is null
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public Cell(Cell neighbor, Direction direction, boolean force) {
        if (neighbor == null) {
            throw new IllegalArgumentException("neighbor");
        }

        switch (direction) {
            case TOP:
                if (neighbor.haveCellAtDirection(Direction.BOTTOM) || force) {
                    neighbor.setCellAtDirection(this, Direction.BOTTOM);
                }
                break;
            case RIGHT:
                if (neighbor.haveCellAtDirection(Direction.LEFT) || force) {
                    neighbor.setCellAtDirection(this, Direction.LEFT);
                }
                break;
            case BOTTOM:
                if (neighbor.haveCellAtDirection(Direction.TOP) || force) {
                    neighbor.setCellAtDirection(this, Direction.TOP);
                }
                break;
            case LEFT:
                if (neighbor.haveCellAtDirection(Direction.RIGHT) || force) {
                    neighbor.setCellAtDirection(this, Direction.RIGHT);
                }
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    // endregion

    /**
     * Get Cell at direction
     *
     * @param direction Direction of where to get the cube
     * @return Returns the cube at the requested direction
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean haveCellAtDirection(Direction direction) {
        switch (direction) {
            case TOP:
                return !haveTopWall();
            case RIGHT:
                return !haveRightWall();
            case BOTTOM:
                return !haveBottomWall();
            case LEFT:
                return !haveLeftWall();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    // region Set Cell At Direction

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @param force     If to force the change (can delete other cell direction)
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction, boolean force) {
        return setCellAtDirection(cell, direction, force, false);
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction) {
        return setCellAtDirection(cell, direction, false, false);
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @param force     If to force the change (can delete other cell direction)
     * @param update    If need to update the cell
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException  Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction, boolean force, boolean update) {
        if (!update && cell == null) {
            throw new IllegalArgumentException("cell");
        }

        switch (direction) {
            case TOP:
                if (!haveTopWall() && !force) {
                    return false;
                }

                setTopWall(false);
                if (update) {
                    cell.setBottomWall(false);
                }

                return true;
            case RIGHT:
                if (!haveRightWall() && !force) {
                    return false;
                }

                setRightWall(false);

                if (update) {
                    cell.setLeftWall(false);
                }

                return true;
            case BOTTOM:
                if (!haveBottomWall() && !force) {
                    return false;
                }

                setBottomWall(false);

                if (update) {
                    cell.setTopWall(false);
                }

                return true;
            case LEFT:
                if (!haveLeftWall() && !force) {
                    return false;
                }

                setLeftWall(false);

                if (update) {
                    cell.setRightWall(false);
                }

                return true;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    // endregion

    /**
     * If Have all walls
     *
     * @return Returns if have all walls
     */
    public boolean haveAllWalls() {
        return this.topWall && this.bottomWall && this.leftWall && this.rightWall;
    }

    // region Candies

    public void addCandy(Candy candy) {
        this.candies.add(candy);
    }

    public void removeCandy(Candy candy) {
        this.candies.remove(candy);
    }

    // region Time Candies

    /**
     * Collect Time Candies and remove them from the cell candies
     *
     * @return Returns all the time candies in this cell
     * @see #collectTimeCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public List<TimeCandy> collectTimeCandy() {
        return this.collectTimeCandy(true);
    }

    /**
     * Collect Time Candies
     *
     * @param removeFoundedCandies Do you want to remove the founded time candies?
     * @return Returns all the time candies in this cell
     * @see #collectTimeCandy() For option to collect & remove candies from cell
     */
    public List<TimeCandy> collectTimeCandy(boolean removeFoundedCandies) {
        List<TimeCandy> timeCandies = this.candies.stream()
                                                  .filter(candy -> candy != null && (candy instanceof TimeCandy ||
                                                                                     candy.getType() ==
                                                                                     CandyPowerType.Time))
                                                  .map(candy -> (TimeCandy) candy)
                                                  .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return timeCandies;
        }

        timeCandies.forEach(timeCandy -> this.candies.remove(timeCandy));

        return timeCandies;
    }

    /**
     * Collect and sum the Time Candies Strength and remove them from the cell
     *
     * @return Returns the sum of the strengths of the time candies
     * @see #collectTimeCandyStrengths(boolean removeFoundedCandies) For option to not remove the candies from to cell
     */
    public int collectTimeCandyStrengths() {
        return this.collectTimeCandyStrengths(true);
    }

    /**
     * Collect Time Candies Strength
     *
     * @param removeFoundedCandies Do you want to remove the founded time candies?
     * @return Returns the sum of the strengths of the time candies
     * @see #collectTimeCandyStrengths() For option to collect & remove time candies from cell
     */
    public int collectTimeCandyStrengths(boolean removeFoundedCandies) {
        List<TimeCandy> timeCandies = this.candies.stream()
                                                  .filter(candy -> candy != null && (candy instanceof TimeCandy ||
                                                                                     candy.getType() ==
                                                                                     CandyPowerType.Time))
                                                  .map(candy -> (TimeCandy) candy)
                                                  .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return timeCandies.stream()
                              .mapToInt(Candy::getCandyStrength)
                              .reduce((candy1, candy2) -> candy1 + candy2)
                              .orElse(0);
        }

        timeCandies.forEach(timeCandy -> this.candies.remove(timeCandy));

        return timeCandies.stream()
                          .mapToInt(Candy::getCandyStrength)
                          .reduce((candy1, candy2) -> candy1 + candy2)
                          .orElse(0);
    }

    // endregion

    // region Points Candies

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public List<PointsCandy> collectPointsCandy() {
        return this.collectPointsCandy(true);
    }

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @param removeFoundedCandies Do you want to remove the founded points candies?
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandy() For option to collect & remove candies from cell
     */
    public List<PointsCandy> collectPointsCandy(boolean removeFoundedCandies) {
        List<PointsCandy> pointsCandies = this.candies.stream()
                                                      .filter(candy -> candy != null && (candy instanceof PointsCandy ||
                                                                                         candy.getType() ==
                                                                                         CandyPowerType.Points))
                                                      .map(candy -> (PointsCandy) candy)
                                                      .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return pointsCandies;
        }

        pointsCandies.forEach(pointCandy -> this.candies.remove(pointCandy));

        return pointsCandies;
    }

    /**
     * Collect and sum the Time Candies Strength and remove them from the cell
     *
     * @return Returns the sum of the strengths of the points candies in this cell
     * @see #collectPointsCandy(boolean removeFoundedCandies) For option to collect without remove candies from cell
     */
    public int collectPointsCandyStrengths() {
        return this.collectPointsCandyStrengths(true);
    }

    /**
     * Collect Points Candies and remove them from the cell candies
     *
     * @param removeFoundedCandies Do you want to remove the founded points candies?
     * @return Returns all the time candies in this cell
     * @see #collectPointsCandyStrengths() For option to collect & remove candies from cell
     */
    public int collectPointsCandyStrengths(boolean removeFoundedCandies) {
        List<PointsCandy> pointsCandies = this.candies.stream()
                                                      .filter(candy -> candy != null && (candy instanceof PointsCandy ||
                                                                                         candy.getType() ==
                                                                                         CandyPowerType.Points))
                                                      .map(candy -> (PointsCandy) candy)
                                                      .collect(Collectors.toList());
        if (!removeFoundedCandies) {
            return pointsCandies.stream()
                                .mapToInt(Candy::getCandyStrength)
                                .reduce((candy1, candy2) -> candy1 + candy2)
                                .orElse(0);
        }

        pointsCandies.forEach(pointCandy -> this.candies.remove(pointCandy));

        return pointsCandies.stream()
                            .mapToInt(Candy::getCandyStrength)
                            .reduce((candy1, candy2) -> candy1 + candy2)
                            .orElse(0);
    }

    // endregion Points Candies

    /**
     * Get the Location (Portal) Candy
     *
     * @return Returns the first Location (Portal) candy that founded
     * @implNote It doesn't remove the candy
     * @see #collectLocationCandyPortal For getting the candy tranfer location
     */
    public PortalCandy collectLocationCandy() {
        return (PortalCandy) this.candies.stream()
                                         .filter(candy -> candy != null && (candy instanceof PortalCandy ||
                                                                            candy.getType() == CandyPowerType.Location))
                                         .findFirst()
                                         .orElse(null);
    }

    /**
     * Get the Location that the Location (Portal) Candy transfer you
     *
     * @return Returns Location that the first Location (Portal) candy that founded
     * @implNote It doesn't remove the candy
     *
     * @see #collectLocationCandy For getting the location candy
     */
    public Tuple<Integer, Integer> collectLocationCandyPortal() {
        return this.candies.stream()
                           .filter(candy -> candy != null && (candy instanceof PortalCandy ||
                                                              candy.getType() == CandyPowerType.Location))
                           .findFirst()
                           .map(candy -> (PortalCandy) candy)
                           .map(PortalCandy::getLocation)
                           .orElse(null);
    }

    // endregion

    // region Getter & Setter

    public boolean haveTopWall() {
        return topWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public boolean haveRightWall() {
        return rightWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public boolean haveBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public boolean haveLeftWall() {
        return leftWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public ArrayList<Candy> getCandies() {
        return this.candies;
    }

    // endregion
}
