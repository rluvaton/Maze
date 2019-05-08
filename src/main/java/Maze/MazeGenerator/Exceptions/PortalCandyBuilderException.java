package Maze.MazeGenerator.Exceptions;

import Maze.Candy.PortalCandy;

public class PortalCandyBuilderException extends CandyBuilderException {
    public PortalCandyBuilderException(String s, PortalCandy.Builder builder) {
        super(s, builder);
    }
}
