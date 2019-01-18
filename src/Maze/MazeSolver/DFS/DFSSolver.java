package Maze.MazeSolver.DFS;

import Helpers.Direction;
import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;
import Maze.Maze;
import Maze.ELocationType;
import Maze.ELocation;

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
                                                              boolean withTeleportCandy) throws Exception {
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        PathNeighbourResult pathNeighbourResult;
        Tuple<Integer, Integer> maybeNextLoc;
        ELocation eLocation;
        DFSCell tempCell;


        int iterations = 0;
        int maxTries = maze.getHeight() * maze.getWidth() + 5;

        while (!Tuple.compare(current, endPoint) && iterations < maxTries) {
            tempCell = (DFSCell) (maze.getCell(current));
            tempCell.setDeadEnd(true);

            pathNeighbourResult = tempCell.getPathNeighbour(
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.TOP)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.RIGHT)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.BOTTOM)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.LEFT)));

            if (pathNeighbourResult != null) {
                iterations++;
                path.push(current);

                eLocation = (pathNeighbourResult.cell == null) ? maze.checkIfELocation(current, pathNeighbourResult.direction) : null;

                if (eLocation != null && eLocation.getType() == ELocationType.Exit) {
                    current = pathNeighbourResult.getNextLocation(current);
                    continue;
                } else if (eLocation != null && eLocation.getType() == ELocationType.Entrance) {
                    // You don't need to enter the entrance
                    continue;
                } else if (pathNeighbourResult.cell == null) {
                    // If it don't have next cell and not entrance / exit than it's an error with the maze generation
                    throw new Exception("There is an error with the maze generation");
                }

                maybeNextLoc = withTeleportCandy ? pathNeighbourResult.cell.collectLocationCandyPortal() : null;
                if (maybeNextLoc != null) {
                    current = maybeNextLoc.clone();
                } else {
                    current = pathNeighbourResult.getNextLocation(current);
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
                                           boolean withTeleportCandy) throws Exception {
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
     * @throws Exception When There is a problem with the maze generation it throw an error
     * @see #getSolvePath(Maze, Tuple, Tuple, boolean) For getting the steps to solve the maze
     * @see #getSolvePathDistance(Maze, Tuple, Tuple, boolean) For getting the length of the solve path
     */
    public static Stack<Direction> getSolvePathSteps(Maze maze,
                                                     Tuple<Integer, Integer> startPoint,
                                                     Tuple<Integer, Integer> endPoint,
                                                     boolean withTeleportCandy) throws Exception {
        Stack<Direction> steps = new Stack<>();
        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        PathNeighbourResult pathNeighbourResult;
        Tuple<Integer, Integer> maybeNextLoc;
        ELocation eLocation;
        DFSCell tempCell;


        int iterations = 0;
        int maxTries = maze.getHeight() * maze.getWidth() + 5;

        while (!Tuple.compare(current, endPoint) && iterations < maxTries) {
            tempCell = (DFSCell) (maze.getCell(current));
            tempCell.setDeadEnd(true);

            pathNeighbourResult = tempCell.getPathNeighbour(
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.TOP)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.RIGHT)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.BOTTOM)),
                    (DFSCell) (maze.checkIfValidMoveCell(current, Direction.LEFT)));

            if (pathNeighbourResult != null) {
                iterations++;
                path.push(current);

                eLocation = (pathNeighbourResult.cell == null) ? maze.checkIfELocation(current, pathNeighbourResult.direction) : null;
//
//                if (eLocation != null && eLocation.getType() == ELocationType.Exit) {
//                    current = pathNeighbourResult.getNextLocation(current);
//                    continue;
//                } else if (eLocation != null && eLocation.getType() == ELocationType.Entrance) {
//                    // You don't need to enter the entrance
//                    continue;
//                } else if (pathNeighbourResult.cell == null) {
//                    // If it don't have next cell and not entrance / exit than it's an error with the maze generation
//                    throw new Exception("There is an error with the maze generation");
//                }

                maybeNextLoc = withTeleportCandy ? pathNeighbourResult.cell.collectLocationCandyPortal() : null;
                if (maybeNextLoc != null) {
                    current = maybeNextLoc.clone();
                } else {
                    current = pathNeighbourResult.getNextLocation(current);
                }

                steps.push(pathNeighbourResult.direction);
            } else if (!path.isEmpty()) {
                try {
                    current = path.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Path is empty and nowhere to go");
            }
        }

        if (iterations >= maxTries) {
            System.out.println("Error: try to solve until got to max tries - " + iterations);
        } else if (!Tuple.compare(current, endPoint)) {
            System.out.println("Error: Didn't got to the end");
        } else if (steps.isEmpty()) {
            System.out.println("Error: don't know how to solve");
        }

        return steps;
    }
}
