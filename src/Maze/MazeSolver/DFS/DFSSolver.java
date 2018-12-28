package Maze.MazeSolver.DFS;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;

import java.util.Stack;

/**
 * DFS Solver
 *
 * @description DFS Algorithm to solve the maze
 */
public class DFSSolver {

    /**
     * The mazeData
     */
    private Cell[][] mazeData;

    /**
     * Get steps to solving the maze from starting to ending location
     *
     * @param maze       Maze cells
     * @param startPoint Location of a starting point
     * @param endPoint   Location of an ending point
     * @return The steps to the solving path
     * @see #getSolvePathDistance(DFSCell[][], Tuple, Tuple) For getting the length of the solved path
     * @see #getSolvePathSteps(DFSCell[][], Tuple, Tuple) Get the Solve steps with the errors (the path that the DFS trying to solve)
     */
    public static Stack<Tuple<Integer, Integer>> getSolvePath(DFSCell[][] maze,
                                                              Tuple<Integer, Integer> startPoint,
                                                              Tuple<Integer, Integer> endPoint) {
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        Tuple<Integer, Integer> nextRes;

        int iterations = 0;
        int maxTries = maze.length * maze[0].length + 5;

        while (!Utils.Instance.compareTuples(current, endPoint) && iterations < maxTries) {
            maze[current.item1][current.item2].setDeadEnd(true);
            nextRes = maze[current.item1][current.item2].getPathNeighbour(
                    maze[current.item1][current.item2].haveTopWall() ? null : maze[current.item1 - 1][current.item2],
                    maze[current.item1][current.item2].haveRightWall() ? null : maze[current.item1][current.item2 + 1],
                    maze[current.item1][current.item2].haveBottomWall() ? null : maze[current.item1 + 1][current.item2],
                    maze[current.item1][current.item2].haveLeftWall() ? null : maze[current.item1][current.item2 - 1]);

            if (nextRes != null) {
                iterations++;
                path.push(current);
                current = new Tuple<>(current.item1 + nextRes.item1, current.item2 + nextRes.item2);
            } else if (!path.isEmpty()) {
                try {
                    current = path.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return (iterations >= maxTries) ? null : path;
    }

    /**
     * Get Solve Path Distance from starting to ending location
     *
     * @param maze       Maze cells
     * @param startPoint Location of a starting point
     * @param endPoint   Location of an ending point
     * @return The length of the solved path
     * @see #getSolvePath(DFSCell[][], Tuple, Tuple) For getting the steps to solve the maze
     * @see #getSolvePathSteps(DFSCell[][], Tuple, Tuple) Get the Solve steps with the errors (the path that the DFS trying to solve)
     */
    public static int getSolvePathDistance(DFSCell[][] maze,
                                           Tuple<Integer, Integer> startPoint,
                                           Tuple<Integer, Integer> endPoint) {
        Stack<Tuple<Integer, Integer>> path = getSolvePath(maze, startPoint, endPoint);
        return path == null ? -1 : path.size();
    }

    /**
     * Get the Solve steps with the errors (the path that the DFS trying to solve)
     *
     * @param maze       Maze cells
     * @param startPoint Location of a starting point
     * @param endPoint   Location of an ending point
     * @return The tries direction from the starting point to end point
     * @see #getSolvePath(DFSCell[][], Tuple, Tuple) For getting the steps to solve the maze
     * @see #getSolvePathDistance(DFSCell[][], Tuple, Tuple) For getting the length of the solve path
     */
    public static Stack<Direction> getSolvePathSteps(DFSCell[][] maze,
                                                                   Tuple<Integer, Integer> startPoint,
                                                                   Tuple<Integer, Integer> endPoint) {
        Stack<Direction> steps = new Stack<>();
        Direction direction;
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        Tuple<Integer, Integer> nextRes;

        int iterations = 0;
        int maxTries = maze.length * maze[0].length + 5;

        while (!Utils.Instance.compareTuples(current, endPoint) && iterations < maxTries) {
            maze[current.item1][current.item2].setDeadEnd(true);
            nextRes = maze[current.item1][current.item2].getPathNeighbour(
                    maze[current.item1][current.item2].haveTopWall() ? null : maze[current.item1 - 1][current.item2],
                    maze[current.item1][current.item2].haveRightWall() ? null : maze[current.item1][current.item2 + 1],
                    maze[current.item1][current.item2].haveBottomWall() ? null : maze[current.item1 + 1][current.item2],
                    maze[current.item1][current.item2].haveLeftWall() ? null : maze[current.item1][current.item2 - 1]);

            direction = Utils.Instance.getDirection(current, nextRes);

            if (nextRes != null) {
                iterations++;
                path.push(current);
                steps.push(direction);
                current = new Tuple<>(current.item1 + nextRes.item1, current.item2 + nextRes.item2);
            } else if (!path.isEmpty()) {
                try {
                    current = path.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return steps;
    }
}
