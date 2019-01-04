package Maze.MazeSolver.DFS;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;

import java.util.Stack;

/**
 * DFS Solver
 *
 * @description DFS Algorithm to solve the maze
 */
public class DFSSolver {

    /**
     * Get steps to solving the maze from starting to ending location
     *
     * @param maze              Maze cells
     * @param startPoint        Location of a starting point
     * @param endPoint          Location of an ending point
     * @param withTeleportCandy Get the solve path with the candies teleportation
     * @return The steps to the solving path
     * @see #getSolvePathDistance(Maze, Tuple, Tuple, boolean) For getting the length of the solved path
     * @see #getSolvePathSteps(Maze, Tuple, Tuple, boolean) Get the Solve steps with the errors (the path that the DFS trying to solve)
     */
    public static Stack<Tuple<Integer, Integer>> getSolvePath(Maze maze,
                                                              Tuple<Integer, Integer> startPoint,
                                                              Tuple<Integer, Integer> endPoint,
                                                              boolean withTeleportCandy) {
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        Tuple<Integer, Integer> nextRes;
        Tuple<Integer, Integer> maybeNextLoc;

        DFSCell tempCell;

        int iterations = 0;
        int maxTries = maze.getHeight() * maze.getWidth() + 5;

        while (!Tuple.compare(current, endPoint) && iterations < maxTries) {
            tempCell = (DFSCell) (maze.getCell(current));
            tempCell.setDeadEnd(true);

            nextRes = tempCell.getPathNeighbour(
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.TOP)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.RIGHT)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.BOTTOM)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.LEFT)));

            if (nextRes != null) {
                iterations++;
                path.push(current);
                maybeNextLoc = maze.getCell(nextRes).collectLocationCandyPortal();
                if (maybeNextLoc != null) {
                    nextRes = maybeNextLoc;
                    current = nextRes.clone();
                } else {
                    current = new Tuple<>(current.item1 + nextRes.item1, current.item2 + nextRes.item2);
                }
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
     * @param maze              Maze cells
     * @param startPoint        Location of a starting point
     * @param endPoint          Location of an ending point
     * @param withTeleportCandy Get the solve path with the candies teleportation
     * @return The length of the solved path
     * @see #getSolvePath(Maze, Tuple, Tuple, boolean) For getting the steps to solve the maze
     * @see #getSolvePathSteps(Maze, Tuple, Tuple, boolean) Get the Solve steps with the errors (the path that the DFS trying to solve)
     */
    public static int getSolvePathDistance(Maze maze,
                                           Tuple<Integer, Integer> startPoint,
                                           Tuple<Integer, Integer> endPoint,
                                           boolean withTeleportCandy) {
        Stack<Tuple<Integer, Integer>> path = getSolvePath(maze, startPoint, endPoint, withTeleportCandy);
        return path == null ? -1 : path.size();
    }

    /**
     * Get the Solve steps with the errors (the path that the DFS trying to solve)
     *
     * @param maze              Maze cells
     * @param startPoint        Location of a starting point
     * @param endPoint          Location of an ending point
     * @param withTeleportCandy Get the solve path with the candies teleportation
     * @return The tries direction from the starting point to end point
     * @see #getSolvePath(Maze, Tuple, Tuple, boolean) For getting the steps to solve the maze
     * @see #getSolvePathDistance(Maze, Tuple, Tuple, boolean) For getting the length of the solve path
     */
    public static Stack<Direction> getSolvePathSteps(Maze maze,
                                                     Tuple<Integer, Integer> startPoint,
                                                     Tuple<Integer, Integer> endPoint,
                                                     boolean withTeleportCandy) {
        Stack<Direction> steps = new Stack<>();
        Direction direction;
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        Tuple<Integer, Integer> nextRes;
        Tuple<Integer, Integer> maybeNextLoc;


        DFSCell tempCell;

        int iterations = 0;
        int maxTries = maze.getHeight() * maze.getWidth() + 5;

        while (!Tuple.compare(current, endPoint) && iterations < maxTries) {
            tempCell = (DFSCell) (maze.getCell(current));
            tempCell.setDeadEnd(true);

            nextRes = tempCell.getPathNeighbour(
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.TOP)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.RIGHT)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.BOTTOM)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.LEFT)));


            direction = Utils.Instance.getDirection(current, nextRes);

            if (nextRes != null) {
                iterations++;
                path.push(current);
                steps.push(direction);

                maybeNextLoc = maze.getCell(nextRes).collectLocationCandyPortal();
                if (maybeNextLoc != null) {
                    nextRes = maybeNextLoc;
                    current = nextRes.clone();
                } else {
                    current = new Tuple<>(current.item1 + nextRes.item1, current.item2 + nextRes.item2);
                }
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
