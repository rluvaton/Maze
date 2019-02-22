package Maze.Solver.Adapter;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;
import Maze.Maze;

import java.util.function.Function;

public abstract class SolverAdapter {

    /**
     * Solve Maze
     *
     * @param maze        Maze to solve
     * @param start       Starting location to solve from
     * @param end         Ending Location to solve from
     * @param withCandies Solve maze with candies
     * @return Direction of the path that solved
     * @throws Exception When the maze can't be solved
     */
    public abstract Direction[] solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception;

    /**
     * Solve Maze
     *
     * @param maze         Maze to solve
     * @param start        Starting location to solve from
     * @param end          Ending Location to solve from
     * @param withCandies  Solve maze with candies
     * @param stepCallback Callback for each step that occurred
     * @return Direction of the path that solved
     * @throws Exception When the maze can't be solved
     */
    public abstract Direction[] solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies, Function<Direction, Void> stepCallback) throws Exception;
}
