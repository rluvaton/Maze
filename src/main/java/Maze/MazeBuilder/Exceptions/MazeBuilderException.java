package Maze.MazeBuilder.Exceptions;

import Maze.MazeBuilder.IMazeBuilder;

public class MazeBuilderException extends Exception {
    private final IMazeBuilder mazeBuilder;

    public MazeBuilderException(IMazeBuilder mazeBuilder, String message) {
        super(message);
        this.mazeBuilder = mazeBuilder;
    }
}
