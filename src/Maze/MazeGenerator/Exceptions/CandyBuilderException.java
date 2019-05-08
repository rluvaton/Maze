package Maze.MazeGenerator.Exceptions;

import Maze.Candy.Candy;
import Maze.Candy.PortalCandy;

public class CandyBuilderException extends Exception {
    private final Candy.Builder builder;

    public static CandyBuilderException createForType(String s, Candy.Builder builder) {
        CandyBuilderException exception;

        if(builder instanceof PortalCandy.Builder) {
            exception = new PortalCandyBuilderException(s, (PortalCandy.Builder) builder);
        } else {
            exception = new CandyBuilderException(s, builder);
        }

        return exception;
    }

    public CandyBuilderException(String s, Candy.Builder builder) {
        super(s);
        this.builder = builder;
    }

    public Candy.Builder getBuilder() {
        return builder;
    }
}
