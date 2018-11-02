package Maze.MazeSolver.DFS;

import Helpers.Tuple;
import Helpers.Utils;
import Maze.Cell;

import java.util.Stack;

/**
 * DFS Algorithm to solve the maze
 */
public class DFSSolver {

    /**
     * The mazeData
     */
    private Cell[][] mazeData;

    public static int getSolvePathDist(DFSCell[][] maze,
                                       Tuple<Integer, Integer> startPoint,
                                       Tuple<Integer, Integer> endPoint) {

        Stack<Tuple<Integer, Integer>> path = new Stack<>();
        Tuple<Integer, Integer> current = startPoint;
        Tuple<Integer, Integer> nextRes;

        int distance = 0;
        int iterations = 0;
        int maxTries = maze.length * maze[0].length + 5;

        while (!Utils.Instance.compareTuples(current, endPoint) && iterations < maxTries) {
            maze[current.item1][current.item2].setDeadEnd(true);
            nextRes = maze[current.item1][current.item2].getPathNeighbour(
                    maze[current.item1][current.item2].haveTopWall() ? null : maze[current.item1 - 1][current.item2],
                    maze[current.item1][current.item2].haveRightWall() ? null : maze[current.item1][current.item2 + 1],
                    maze[current.item1][current.item2].haveBottomWall() ? null : maze[current.item1 + 1][current.item2],
                    maze[current.item1][current.item2].haveLeftWall() ? null : maze[current.item1][current.item2 - 1]
            );

            if (nextRes != null) {
                iterations++;
                path.push(current);
                current = new Tuple<>(current.item1 + nextRes.item1, current.item2 + nextRes.item2);;
            } else if (!path.isEmpty()) {
                try {
                    current = path.pop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }

        return (iterations >= maxTries) ? -1 : path.size();
    }
}
