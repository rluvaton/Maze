package Maze.MazeGenerator;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.Utils;
import Maze.ELocationType;
import Maze.Maze;
import Maze.MazeBuilder.IMazeBuilder;
import Maze.MazeBuilder.IMazeBuilder.ELocationBaseData;
import Maze.Solver.Adapter.SolverAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import static Helpers.Utils.Instance;

public class MazeGenerator {

    private IMazeBuilder mazeBuilder;
    private SolverAdapter solverAdapter;

    private int height;
    private int width;

    private int eLocationOptionsCount = -1;

    private ArrayList<ELocationBaseData> entrances = new ArrayList<>();
    private ArrayList<ELocationBaseData> exits = new ArrayList<>();

    public MazeGenerator(IMazeBuilder mazeBuilder, SolverAdapter solverAdapter) {
        assert mazeBuilder != null && solverAdapter != null;
        this.mazeBuilder = mazeBuilder;
        this.solverAdapter = solverAdapter;
    }

    public Maze generate(int height,
                         int width,
                         int minDistance,
                         int numOfEntrance,
                         int numOfExits) {
        this.height = height;
        this.width = width;

        this.mazeBuilder
                .buildMazeSkeleton(height, width)
                .buildAllCellsAsEmpty();

        new DFSMazeGenerator(this.mazeBuilder, height, width)
                .randomizeMaze(Utils.Instance.generateCoordinate(height, width));

        this.createRandomEntrancesAndExists(numOfEntrance, numOfExits, minDistance);

        return this.mazeBuilder.getMaze();
    }

    private void createRandomEntrancesAndExists(int numOfEntrances, int numOfExits, int minDistance) {
        assert numOfEntrances > 0 && numOfExits > 0;

        this.validateMinDistance(minDistance);

        this.eLocationOptionsCount = height * 2 + width * 2;

        // Because we don't wanna decrease by 1 every loop cycle
//        numOfEntrances--;
//        numOfExits--;

        int entranceIndex = 0;
        int exitIndex = 0;

        while (this.entrances.size() < numOfEntrances || this.exits.size() < numOfExits) {
            if (this.entrances.size() < numOfEntrances) {
                try {
                    ELocationBaseData entrance = randomizeEntrance(minDistance);

                    this.entrances.add(entrance);
//                    entranceIndex++;
                } catch (Exception e) {
                    System.out.println("Retry after entrance not founded...");
                    this.removeLastItem(exits);
                }
            }

            if (this.exits.size() < numOfExits) {
                try {
                    ELocationBaseData exit = randomizeExit(minDistance);

                    exits.add(exit);
                } catch (Exception e) {
                    System.out.println("Retry after exit not founded...");
                    this.removeLastItem(entrances);
                }
            }
        }

        this.mazeBuilder.buildManyEntrances(entrances);
        this.mazeBuilder.buildManyExits(exits);
    }

    private void removeLastItem(ArrayList<ELocationBaseData> eLocationBaseData) {
        if (!eLocationBaseData.isEmpty()) {
            eLocationBaseData.remove(eLocationBaseData.size() - 1);
        }
    }

    private void validateMinDistance(int minDistance) {
        if (width * height < minDistance) {
            throw new IllegalArgumentException("min distance isn't possible");
        }
    }

    private ELocationBaseData randomizeEntrance(int minDistance) throws Exception {
        return randomizeELocation(minDistance, ELocationType.Entrance);
    }

    private ELocationBaseData randomizeExit(int minDistance) throws Exception {
        return randomizeELocation(minDistance, ELocationType.Exit);
    }

    private ELocationBaseData randomizeELocation(int minDistance, ELocationType type) throws Exception {
        int maxTries = getMaxTries();
        int tryNo = 0;

        ELocationBaseData entrance;

        do {
            entrance = this.generateRandomELocationAtBorder();
        } while (!this.isValidELocation(entrance, minDistance, type) && ++tryNo < maxTries);

        if (tryNo >= maxTries) {
            throw new Exception("Tried " + tryNo + "and no valid " + type.toString() + "founded");
        }

        return entrance;
    }

    private int getMaxTries() {
        return (int) ((this.height * 2 + this.width * 2) * 1.5);
    }

    private ELocationBaseData generateRandomELocationAtBorder() {
        return Instance.getRandomState()
                ? this.generateRandomCoordinateAtHorizontalBorder()
                : this.generateRandomCoordinateAtVerticalBorder();
    }

    private ELocationBaseData generateRandomCoordinateAtHorizontalBorder() {
        int column;
        Direction direction;

        if (Instance.getRandomState()) {
            column = 0;
            direction = Direction.LEFT;
        } else {
            column = width;
            direction = Direction.RIGHT;
        }

        return new ELocationBaseData(new Coordinate(Instance.getRandomNumber(height), column), direction);
    }

    private ELocationBaseData generateRandomCoordinateAtVerticalBorder() {
        int row;
        Direction direction;

        if (Instance.getRandomState()) {
            row = 0;
            direction = Direction.TOP;
        } else {
            row = height;
            direction = Direction.BOTTOM;
        }

        return new ELocationBaseData(new Coordinate(row, Instance.getRandomNumber(width)), direction);
    }

    private boolean isValidELocation(ELocationBaseData optionalELocation, int minDistance, ELocationType type) {
        assert optionalELocation != null && type != null;

        if (!Instance.inLimits(optionalELocation, this.width, this.height)) {
            return false;
        }

        if (minDistance <= 0) {
            return checkIfELocationDontExistAlready(optionalELocation);
        }

        Coordinate eLocationPos = optionalELocation.getPos();
        Maze maze = this.mazeBuilder.getMaze();

        ArrayList<ELocationBaseData> sameELocationTypes;
        ArrayList<ELocationBaseData> diffELocationTypes;

        if (type == ELocationType.Entrance) {
            sameELocationTypes = entrances;
            diffELocationTypes = exits;
        } else {
            sameELocationTypes = exits;
            diffELocationTypes = entrances;
        }

        return sameELocationTypes.stream().noneMatch(optionalELocation::equals) &&
                diffELocationTypes.stream().allMatch((diffELocationType) -> checkELocation(diffELocationType, optionalELocation, minDistance, eLocationPos, maze));
    }

    private boolean checkELocation(ELocationBaseData diffELocationType, ELocationBaseData optionalELocation, int minDistance, Coordinate eLocationPos, Maze maze) {
        try {
            boolean isPositionsEquals = optionalELocation.equals(diffELocationType);
            return !isPositionsEquals && diffELocationType != null && this.solverAdapter.solveMaze(maze, diffELocationType.getPos(), eLocationPos, false).length >= minDistance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkIfELocationDontExistAlready(ELocationBaseData optionalELocation) {
        assert optionalELocation != null;
        return entrances.stream().noneMatch(optionalELocation::equals) &&
                exits.stream().noneMatch(optionalELocation::equals);
    }

    class DFSMazeGenerator {
        private IMazeBuilder mazeBuilder;
        Stack<Coordinate> steps;
        boolean[][] visited;

        private final int height;
        private final int width;

        public DFSMazeGenerator(IMazeBuilder mazeBuilder, int height, int width) {
            assert mazeBuilder != null;
            this.mazeBuilder = mazeBuilder;

            this.steps = new Stack<>();
            this.visited = new boolean[height][width];
            this.height = height;
            this.width = width;
        }

        private void randomizeMaze(Coordinate start) {
            Coordinate currentPosition = start;
            this.visitCoordinates(currentPosition);
            this.steps.push(currentPosition);

            while (!this.steps.isEmpty()) {
                Coordinate nextPos = this.getRandomItemFromArray(getValidNeighbors(currentPosition));

                if (nextPos != null) {
                    try {
                        mazeBuilder.buildDoor(currentPosition, nextPos);
                    } catch (Exception e) {
                        handleBuildDoorException(e);
                    }

                    this.steps.push(nextPos);
                    this.visitCoordinates(nextPos);
                    currentPosition = nextPos;
                } else {
                    currentPosition = this.steps.pop();
                }
            }

        }

        private void handleBuildDoorException(Exception e) {
            e.printStackTrace();
        }

        private Coordinate[] getValidNeighbors(Coordinate currPosition) {
            return Arrays.stream(Direction.values())
                    .map(direction -> Utils.Instance.moveCoordinatesToDirection(currPosition, direction))
                    .filter(position -> isCoordinatesInBound(position) && !isCoordinatesVisited(position))
                    .toArray(Coordinate[]::new);
        }

        private boolean isCoordinatesInBound(Coordinate position) {
            return Utils.Instance.inBounds(position, this.height, this.width);
        }

        private Coordinate getRandomItemFromArray(Coordinate[] coordinates) {
            int size = coordinates.length;
            if (size == 0) {
                return null;
            }
            int index = size == 1 ? 0 : Utils.Instance.getRandomNumber(size);
            return coordinates[index];
        }

        private void visitCoordinates(Coordinate position) {
            this.visited[position.getRow()][position.getColumn()] = true;
        }

        private boolean isCoordinatesVisited(Coordinate position) {
            return this.visited[position.getRow()][position.getColumn()];
        }
    }
}
