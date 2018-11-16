package Maze;

import Maze.Candy.Candy;

import java.util.ArrayList;

public class Cell
{
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

    public Cell()
    {
    }

    /**
     * Create Cube with one open neighbor
     *
     * @param neighbor  Neighbor to add
     * @param direction Direction of where to add the cube
     * @throws IllegalArgumentException  Throw Error if neighbor is null
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public Cell(Cell neighbor, Direction direction)
    {
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
    public Cell(Cell neighbor, Direction direction, boolean force)
    {
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

    /**
     * Get Cell at direction
     *
     * @param direction Direction of where to get the cube
     * @return Returns the cube at the requested direction
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean haveCellAtDirection(Direction direction)
    {
        switch (direction) {
            case TOP:
                return !haveTopWall();
            case RIGHT:
                return !haveRightWall();
            case BOTTOM:
                return !haveBottomWall();
            case LEFT:
                return haveLeftWall();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @param force     If to force the change (can delete other cell direction)
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException           Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction, boolean force)
    {
        return setCellAtDirection(cell, direction, force, false);
    }

    /**
     * Set Cube at specific direction
     *
     * @param cell      The new cell
     * @param direction The direction of where to set the new cell
     * @return Returns if set the cell or not
     * @throws IllegalArgumentException           Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction)
    {
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
     * @throws IllegalArgumentException           Throw if cell is null and update is false
     * @throws IndexOutOfBoundsException Throw if direction is not recognized
     */
    public boolean setCellAtDirection(Cell cell, Direction direction, boolean force, boolean update)
    {
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

    /**
     * If Have all walls
     * @return Returns if have all walls
     */
    public boolean haveAllWalls() {
        return this.topWall && this.bottomWall && this.leftWall && this.rightWall;
    }

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

    public void addCandy(Candy candy) {
        this.candies.add(candy);
    }

    public ArrayList<Candy> getCandies() {
        return this.candies;
    }


    // endregion
}
