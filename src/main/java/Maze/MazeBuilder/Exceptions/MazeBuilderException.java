package Maze.MazeBuilder.Exceptions;

import Helpers.Builder.BuilderException;
import Maze.MazeBuilder.IMazeBuilder;

public class MazeBuilderException extends BuilderException {
    private final IMazeBuilder mazeBuilder;

    public MazeBuilderException(IMazeBuilder mazeBuilder, String message) {
        super("Maze", message);
        this.mazeBuilder = mazeBuilder;
    }
}
