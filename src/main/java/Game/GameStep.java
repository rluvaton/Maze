package Game;

import Helpers.Coordinate;
import Maze.Candy.CandyPowerType;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.GenerateCandyConfig;
import Maze.MazeGenerator.IntegerConfiguration;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import player.ComputerPlayer.ComputerPlayer;

public class GameStep {
    public final static GameStep VERY_EASY = getVeryEasyGameStep();
    public final static GameStep EASY = getEasyGameStep();
    public final static GameStep MEDIUM = getMediumGameStep();
    public final static GameStep HARD = getHardGameStep();
    public final static GameStep VERY_HARD = getVeryHardGameStep();

    private IntegerConfiguration minDistance;

    private IntegerConfiguration entrancesCount;
    private IntegerConfiguration exitsCount;

    private IntegerConfiguration height;
    private IntegerConfiguration width;

    private boolean withComputerPlayer = false;
    private IntegerConfiguration computerPlayerSpeed = null;

    private GenerateCandyConfig candyConfig;
    private IntegerConfiguration totalCandies = new IntegerConfiguration(0);

    private static GameStep getVeryEasyGameStep() {
        GameStep step = new GameStep();
        IntegerConfiguration edge = new IntegerConfiguration(4);

        step.setHeight(edge);
        step.setWidth(edge);

        step.setMinDistance(new IntegerConfiguration(0));

        step.setEntrancesCount(new IntegerConfiguration(1));
        step.setExitsCount(new IntegerConfiguration(1));

        step.setWithComputerPlayer(false);
        step.setComputerPlayerSpeed(null);

        return step;
    }

    private static GameStep getEasyGameStep() {
        GameStep step = new GameStep();
        IntegerConfiguration edge = new IntegerConfiguration(5, 8);

        step.setHeight(edge);
        step.setWidth(edge);

        step.setMinDistance(new IntegerConfiguration(0, 3));

        step.setEntrancesCount(new IntegerConfiguration(1));
        step.setExitsCount(new IntegerConfiguration(0, 3));

        step.setWithComputerPlayer(false);
        step.setComputerPlayerSpeed(null);

        return step;
    }

    private static GameStep getMediumGameStep() {
        GameStep step = new GameStep();
        IntegerConfiguration edge = new IntegerConfiguration(10, 15);

        step.setHeight(edge);
        step.setWidth(edge);

        step.setMinDistance(new IntegerConfiguration(5, 10));

        step.setEntrancesCount(new IntegerConfiguration(1));
        step.setExitsCount(new IntegerConfiguration(1));

        step.setWithComputerPlayer(false);
        step.setComputerPlayerSpeed(null);

        step.setTotalCandies(new IntegerConfiguration(4, 7));

        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(1, 200))
                .setTypes(new CandyPowerType[]{CandyPowerType.Points})
                .setTimeToLive(new IntegerConfiguration(-1));

        step.setCandyConfig(candyConfig);

        return step;
    }

    private static GameStep getHardGameStep() {
        GameStep step = new GameStep();
        IntegerConfiguration edge = new IntegerConfiguration(15, 20);

        step.setHeight(edge);
        step.setWidth(edge);

        step.setMinDistance(new IntegerConfiguration(15));

        step.setEntrancesCount(new IntegerConfiguration(1));
        step.setExitsCount(new IntegerConfiguration(1));

        step.setWithComputerPlayer(true);
        step.setComputerPlayerSpeed(new IntegerConfiguration(500));

        step.setTotalCandies(new IntegerConfiguration(10, 15));

        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(-200, 200))
                .setTypes(new CandyPowerType[]{CandyPowerType.Points, CandyPowerType.Time});

        step.setCandyConfig(candyConfig);

        return step;
    }

    private static GameStep getVeryHardGameStep() {
        GameStep step = new GameStep();
        IntegerConfiguration edge = new IntegerConfiguration(25, 30);

        step.setHeight(edge);
        step.setWidth(edge);

        step.setMinDistance(new IntegerConfiguration(5, 10));

        step.setEntrancesCount(new IntegerConfiguration(1));
        step.setExitsCount(new IntegerConfiguration(1));

        step.setWithComputerPlayer(true);
        step.setComputerPlayerSpeed(new IntegerConfiguration(200, 500));

        step.setTotalCandies(new IntegerConfiguration(10, 15));

        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(-200, 200))
                .setTypes(CandyPowerType.values())
                .setTimeToLive(new IntegerConfiguration(10000, 35000));

        step.setCandyConfig(candyConfig);

        return step;
    }

    private GameStep() {
    }

    public IntegerConfiguration getMinDistance() {
        return minDistance;
    }

    public GameStep setMinDistance(IntegerConfiguration minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public IntegerConfiguration getEntrancesCount() {
        return entrancesCount;
    }

    public GameStep setEntrancesCount(IntegerConfiguration entrancesCount) {
        this.entrancesCount = entrancesCount;
        return this;
    }

    public IntegerConfiguration getExitsCount() {
        return exitsCount;
    }

    public GameStep setExitsCount(IntegerConfiguration exitsCount) {
        this.exitsCount = exitsCount;
        return this;
    }

    public IntegerConfiguration getHeight() {
        return height;
    }

    public GameStep setHeight(IntegerConfiguration height) {
        this.height = height;
        return this;
    }

    public IntegerConfiguration getWidth() {
        return width;
    }

    public GameStep setWidth(IntegerConfiguration width) {
        this.width = width;
        return this;
    }

    public boolean isWithComputerPlayer() {
        return withComputerPlayer;
    }

    public GameStep setWithComputerPlayer(boolean withComputerPlayer) {
        this.withComputerPlayer = withComputerPlayer;
        return this;
    }

    public IntegerConfiguration getComputerPlayerSpeed() {
        return computerPlayerSpeed;
    }

    public GameStep setComputerPlayerSpeed(IntegerConfiguration computerPlayerSpeed) {
        this.computerPlayerSpeed = computerPlayerSpeed;
        return this;
    }

    public GenerateCandyConfig getCandyConfig() {
        return candyConfig;
    }

    public GameStep setCandyConfig(GenerateCandyConfig candyConfig) {
        this.candyConfig = candyConfig;
        return this;
    }

    public IntegerConfiguration getTotalCandies() {
        return totalCandies;
    }

    public GameStep setTotalCandies(IntegerConfiguration totalCandies) {
        this.totalCandies = totalCandies;
        return this;
    }

    public Maze build() throws MazeBuilderException {
        return new MazeGenerator(new RectangleMazeBuilder(), new BFSSolverAdapter())
                    .generateMaze(this.height.getValue(), this.width.getValue())
                    .createRandomEntrancesAndExists(this.entrancesCount.getValue(), this.exitsCount.getValue(), this.getMinDistance().getValue())
                    .generateRandomCandies(this.candyConfig, totalCandies.getValue())
                    .create();
    }

    public ComputerPlayer getPlayer() {
        if(!isWithComputerPlayer()) {
            return null;
        }

        return new ComputerPlayer(new Coordinate(0, 0), this.computerPlayerSpeed.getValue());
    }
}
