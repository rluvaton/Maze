package Maze.Solver.Adapter;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Tuple;
import Maze.Maze;
import Maze.Cell;

import java.util.List;
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
    public abstract List<Direction> solveMaze(Maze maze, Coordinate start, Coordinate end, boolean withCandies) throws Exception;

    public abstract SolverAdapter clone();
}
