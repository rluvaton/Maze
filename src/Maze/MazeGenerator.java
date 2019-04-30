package Maze;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Utils;
import Maze.MazeBuilder.IMazeBuilder;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MazeGenerator {

    private IMazeBuilder mazeBuilder;

    public MazeGenerator(IMazeBuilder mazeBuilder) {
        assert mazeBuilder != null;
        this.mazeBuilder = mazeBuilder;
    }

    public Maze generate(int height,
                         int width,
                         int minDistance,
                         int numberOfEntrance,
                         int numberOfExists) {
        this.mazeBuilder
                .buildMazeSkeleton(height, width)
                .buildAllCellsAsEmpty();

        new DFSGenerator(this.mazeBuilder, height, width)
                .run(this.getRandomCoordinatesInBound(height, width));

        return this.mazeBuilder.getMaze();
    }

    private Coordinate getRandomCoordinatesInBound(int height, int width) {
        Random r = new Random();
        return new Coordinate(r.nextInt(height) - 1, r.nextInt(width) - 1);

    }

    class DFSGenerator {
        private IMazeBuilder mazeBuilder;
        Stack<Coordinate> steps;
        boolean[][] visited;

        private final int height;
        private final int width;

        public DFSGenerator(IMazeBuilder mazeBuilder, int height, int width) {
            assert mazeBuilder != null;
            this.mazeBuilder = mazeBuilder;

            this.steps = new Stack<>();
            this.visited = new boolean[height][width];
            this.height = height;
            this.width = width;
        }

        private void run(Coordinate start) {
            Coordinate currentPosition = start;
            this.visitCoordinates(currentPosition);
            this.steps.push(currentPosition);

            while (!this.steps.isEmpty()) {
                Coordinate finalCurrentPosition = currentPosition;
                Coordinate nextPos = this.getRandomItemFromArray(
                        Arrays.stream(Direction.values())
                                .map(direction -> Utils.Instance.moveCoordinatesToDirection(finalCurrentPosition, direction))
                                .filter(position -> isCoordinatesInBound(position) && !isCoordinatesVisited(position))
                                .toArray(Coordinate[]::new)
                );

                if (nextPos != null) {
                    mazeBuilder.buildDoor(currentPosition, nextPos);
                    this.steps.push(nextPos);
                    this.visitCoordinates(nextPos);
                    currentPosition = nextPos;
                } else {
                    currentPosition = this.steps.pop();
                }
            }

        }

        private boolean isCoordinatesInBound(Coordinate position) {
            return Utils.Instance.inBounds(position, this.height, this.width);
        }

        private Coordinate getRandomItemFromArray(Coordinate[] coordinates) {
            int size = coordinates.length;
            if (size == 0) {
                return null;
            } else if (size == 1) {
                return coordinates[0];
            } else {
                Random r = new Random();
                return coordinates[r.nextInt(size - 1)];
            }
        }

        private void visitCoordinates(Coordinate position) {
            this.visited[position.getRow()][position.getColumn()] = true;
        }

        private boolean isCoordinatesVisited(Coordinate position) {
            return this.visited[position.getRow()][position.getColumn()];
        }
    }
}
