package Maze.MazeGenerator;

import Helpers.Coordinate;
import Helpers.Direction;
import Helpers.RandomHelper;
import Helpers.Utils;
import Maze.Candy.Candy;
import Maze.Candy.CandyPowerType;
import Maze.Candy.PortalCandy;
import Maze.Cell;
import Maze.ELocationType;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.IMazeBuilder;
import Maze.MazeBuilder.IMazeBuilder.ELocationBaseData;
import Maze.MazeGenerator.Exceptions.CandyBuilderException;
import Maze.MazeGenerator.Exceptions.PortalCandyBuilderException;
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

    private boolean parseActionAlready = false;

    MazeGeneratorActions mazeGeneratorActions = new MazeGeneratorActions();

    public MazeGenerator(IMazeBuilder mazeBuilder, SolverAdapter solverAdapter) {
        assert mazeBuilder != null && solverAdapter != null;
        this.mazeBuilder = mazeBuilder;
        this.solverAdapter = solverAdapter;
    }

    public MazeGenerator generateMaze(int height,
                                      int width) {
        this.mazeGeneratorActions.add(MazeGeneratorActions.MazeGenerationStepTypes.GENERATE_MAZE, () -> {
            this.height = height;
            this.width = width;

            this.mazeBuilder
                    .buildMazeSkeleton(height, width)
                    .buildAllCellsAsEmpty();

            new DFSMazeGenerator(this.mazeBuilder, height, width)
                    .randomizeMaze(RandomHelper.generateCoordinate(height, width));

            return this;
        });

        return this;
    }

    // region Random Entrances & Exists
    public MazeGenerator createRandomEntrancesAndExists(int numOfEntrances, int numOfExits, int minDistance) {
        this.mazeGeneratorActions.add(MazeGeneratorActions.MazeGenerationStepTypes.CREATE_ELOCATION, () -> {
            assert numOfEntrances > 0 && numOfExits > 0;

            this.validateMinDistance(minDistance);

            this.eLocationOptionsCount = height * 2 + width * 2;

            while (this.entrances.size() < numOfEntrances || this.exits.size() < numOfExits) {
                if (this.entrances.size() < numOfEntrances) {
                    try {
                        ELocationBaseData entrance = randomizeEntrance(minDistance);

                        this.entrances.add(entrance);
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

            return this;
        });
        return this;
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

        ELocationBaseData eLocation;

        do {
            eLocation = this.generateRandomELocationAtBorder();
        } while (!this.isValidELocation(eLocation, minDistance, type) && ++tryNo < maxTries);

        if (tryNo >= maxTries) {
            throw new Exception("Tried " + tryNo + "and no valid " + type.toString() + "founded");
        }

        return eLocation;
    }

    private int getMaxTries() {
        return (int) ((this.height * 2 + this.width * 2) * 1.5);
    }

    private ELocationBaseData generateRandomELocationAtBorder() {
        return RandomHelper.getRandomState()
                ? this.generateRandomCoordinateAtHorizontalBorder()
                : this.generateRandomCoordinateAtVerticalBorder();
    }

    private ELocationBaseData generateRandomCoordinateAtHorizontalBorder() {
        int column;
        Direction direction;

        if (RandomHelper.getRandomState()) {
            column = 0;
            direction = Direction.LEFT;
        } else {
            column = width - 1;
            direction = Direction.RIGHT;
        }

        return new ELocationBaseData(new Coordinate(RandomHelper.getRandomNumber(height), column), direction);
    }

    private ELocationBaseData generateRandomCoordinateAtVerticalBorder() {
        int row;
        Direction direction;

        if (RandomHelper.getRandomState()) {
            row = 0;
            direction = Direction.UP;
        } else {
            row = height - 1;
            direction = Direction.DOWN;
        }

        return new ELocationBaseData(new Coordinate(row, RandomHelper.getRandomNumber(width)), direction);
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
        Maze maze = null;
        try {
            maze = this.mazeBuilder.getMaze();
        } catch (MazeBuilderException e) {
            e.printStackTrace();
        }

        ArrayList<ELocationBaseData> sameELocationTypes;
        ArrayList<ELocationBaseData> diffELocationTypes;

        if (type == ELocationType.Entrance) {
            sameELocationTypes = entrances;
            diffELocationTypes = exits;
        } else {
            sameELocationTypes = exits;
            diffELocationTypes = entrances;
        }

        Maze finalMaze = maze;
        return sameELocationTypes.stream().noneMatch(optionalELocation::equals) &&
                diffELocationTypes.stream().allMatch((diffELocationType) -> !optionalELocation.equals(diffELocationType) && validateELocationHasMinDistance(finalMaze, diffELocationType, eLocationPos, minDistance));
    }

    private boolean validateELocationHasMinDistance(Maze maze, ELocationBaseData diffELocationType, Coordinate eLocationPos, int minDistance) {
        try {
            return diffELocationType != null && this.solverAdapter.solveMaze(maze, diffELocationType.getPos(), eLocationPos, false).length >= minDistance;
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

    // endregion

    // region Generate Random Candies

    public MazeGenerator generateRandomCandies(int count) {
        return generateRandomCandies(CandyPowerType.values(), count, false);
    }

    public MazeGenerator generateRandomCandies(CandyPowerType type, int count) {
        return generateRandomCandies(new CandyPowerType[]{type}, count, false);
    }

    public MazeGenerator generateRandomCandies(int count, boolean generateOnlyGood) {
        return generateRandomCandies(CandyPowerType.values(), count, generateOnlyGood);
    }

    public MazeGenerator generateRandomCandies(CandyPowerType[] types, int count, boolean generateOnlyGood) {

        GenerateCandyConfig config = new GenerateCandyConfig()
                .setTypes(types);

        if (generateOnlyGood) {
            config.setStrengthPower(new IntegerConfiguration(1, 1000));
        }

        return this.generateRandomCandies(config, count);
    }

    public MazeGenerator generateRandomCandies(GenerateCandyConfig config, int count) {
        this.mazeGeneratorActions.add(MazeGeneratorActions.MazeGenerationStepTypes.CREATE_CANDIES, () -> {
            Coordinate cellLoc;
            Cell cell;

            for (int i = 0; i < count; i++) {
                cellLoc = RandomHelper.generateCoordinate(this.height, this.width);
                cell = this.mazeBuilder.getCellAtPosition(cellLoc);

                if (cell.hasNeighborELocation()) {
                    i--;
                    continue;
                }

                config.setCellLoc(cellLoc);
                Candy candy = null;

                try {
                    candy = this.generateSingleCandy(config);
                } catch (CandyBuilderException e) {
                    i--;
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cell.addCandy(candy);
            }

            return this;
        });

        return this;
    }

    private Candy generateSingleCandy(GenerateCandyConfig config) throws CandyBuilderException {
        assert config != null;

        // The type of the candy that gonna be generated
        CandyPowerType type = getRandomType(config.getTypes());

        Candy.Builder builder = Candy.Builder.createForType(type)
                .setTimeToLive(config.getTimeToLive().getValue())
                .setCandyStrength(config.getStrengthPower().getValue());

        if (type == CandyPowerType.Location) {
            buildForPortalCandy(config, builder);
        }

        return builder.build();
    }

    private void buildForPortalCandy(GenerateCandyConfig config, Candy.Builder builder) throws PortalCandyBuilderException {
        Coordinate otherCellLocation = config.getOtherCellLocation();

        int totalBuiltCells = mazeBuilder.getTotalBuiltCells() * 2;
        int i = 0;

        while (!isPortalCandyCoordinatesValid(otherCellLocation) && totalBuiltCells > i) {
            otherCellLocation = RandomHelper.generateCoordinate(height, width);
            i++;
        }

        if (totalBuiltCells <= i) {
            throw new PortalCandyBuilderException("Couldn't generate good coordinates for portal candy", (PortalCandy.Builder) builder);
        }

        config.setOtherCellLocation(otherCellLocation);

        ((PortalCandy.Builder) (builder))
                .setOtherSideLocation(otherCellLocation)
                .setOtherSideCell(mazeBuilder.getCellAtPosition(otherCellLocation))
                .setMyLocation(config.getCellLoc());
    }

    private boolean isPortalCandyCoordinatesValid(Coordinate otherCellLocation) {
        return otherCellLocation != null &&
                Instance.inBounds(otherCellLocation, height, width) &&
                mazeBuilder.getCellAtPosition(otherCellLocation).getCandies().stream().noneMatch(candy -> candy.getType() == CandyPowerType.Location) &&
                this.exits.stream().noneMatch(eLocationBaseData -> otherCellLocation.equals(eLocationBaseData.getPos())) &&
                this.entrances.stream().noneMatch(eLocationBaseData -> otherCellLocation.equals(eLocationBaseData.getPos()));
    }

    private CandyPowerType getRandomType(CandyPowerType[] types) {
        return types[RandomHelper.getRandomNumber(types.length)];
    }

    // endregion

    public Maze create() throws MazeBuilderException {
        MazeGenerator mazeGenerator = this.mazeGeneratorActions.parseActions();

        if (mazeGenerator == null && !parseActionAlready) {
            throw new MazeBuilderException(this.mazeBuilder, "Nothing to build, please create generate maze");
        }

        Maze maze = mazeBuilder.getMaze();

        if(maze == null) {
            throw new MazeBuilderException(this.mazeBuilder, "Error at creating the maze");
        }

        maze.setSolverAdapter(this.solverAdapter);

        parseActionAlready = true;

        return maze;
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
            int index = size == 1 ? 0 : RandomHelper.getRandomNumber(size);
            return coordinates[index];
        }

        private void visitCoordinates(Coordinate position) {
            this.visited[position.getRow()][position.getColumn()] = true;
        }

        private boolean isCoordinatesVisited(Coordinate position) {
            return this.visited[position.getRow()][position.getColumn()];
        }
    }



    public static class Builder {
        private IMazeBuilder mazeBuilder;
        private SolverAdapter solverAdapter;

        private int height;
        private int width;
        private int minDistance;
        private int numOfEntrance;
        private int numOfExits;

        private GenerateCandyConfig candyConfig;
        private int totalCandies = 0;

        public Builder() {
        }

        public IMazeBuilder getMazeBuilder() {
            return mazeBuilder;
        }

        public Builder setMazeBuilder(IMazeBuilder mazeBuilder) {
            this.mazeBuilder = mazeBuilder;
            return this;
        }

        public SolverAdapter getSolverAdapter() {
            return solverAdapter;
        }

        public Builder setSolverAdapter(SolverAdapter solverAdapter) {
            this.solverAdapter = solverAdapter;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getMinDistance() {
            return minDistance;
        }

        public Builder setMinDistance(int minDistance) {
            this.minDistance = minDistance;
            return this;
        }

        public int getNumOfEntrance() {
            return numOfEntrance;
        }

        public Builder setNumOfEntrance(int numOfEntrance) {
            this.numOfEntrance = numOfEntrance;
            return this;
        }

        public int getNumOfExits() {
            return numOfExits;
        }

        public Builder setNumOfExits(int numOfExits) {
            this.numOfExits = numOfExits;
            return this;
        }

        public GenerateCandyConfig getCandyConfig() {
            return candyConfig;
        }

        public Builder setCandyConfig(GenerateCandyConfig candyConfig) {
            this.candyConfig = candyConfig;
            return this;
        }

        public int getTotalCandies() {
            return totalCandies;
        }

        public Builder setTotalCandies(int totalCandies) {
            this.totalCandies = totalCandies;
            return this;
        }

        public Maze build() throws MazeBuilderException {
            return new MazeGenerator(this.mazeBuilder, this.solverAdapter)
                    .generateMaze(this.height, this.width)
                    .createRandomEntrancesAndExists(this.numOfEntrance, this.numOfExits, this.minDistance)
                    .generateRandomCandies(this.candyConfig, this.totalCandies)
                    .create();
        }
    }

}

