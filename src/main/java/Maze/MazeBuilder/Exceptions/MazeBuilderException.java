package Maze.MazeBuilder.Exceptions;

import Maze.MazeBuilder.RectangleMazeBuilder;

public class MazeBuilderException extends Throwable {
    private final RectangleMazeBuilder rectangleMazeBuilder;

    public MazeBuilderException(RectangleMazeBuilder rectangleMazeBuilder, String message) {
        super(message);
        this.rectangleMazeBuilder = rectangleMazeBuilder;
    }
}
