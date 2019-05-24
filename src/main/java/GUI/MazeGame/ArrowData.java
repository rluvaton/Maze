package GUI.MazeGame;

import Helpers.Direction;
import Maze.ELocationType;

public class ArrowData {
    public int x;
    public int y;
    public ELocationType type;
    public Direction direction;

    public ArrowData(ELocationType type, Direction direction) {
        this.type = type;
        this.direction = direction;
    }

    public ArrowData(int x, int y, ELocationType type, Direction direction) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.direction = direction;
    }

    public ArrowData setX(int x) {
        this.x = x;
        return this;
    }

    public ArrowData setY(int y) {
        this.y = y;
        return this;
    }

    public ArrowData setPoint(int x, int y) {
        return this.setX(x).setY(y);
    }

    public ArrowData setType(ELocationType type) {
        this.type = type;
        return this;
    }

    public ArrowData setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public Direction getDirectionBasedOnELocationType() {
        return (this.type == ELocationType.Exit) ? direction : Direction.getOppositeDirection(direction);
    }
}
