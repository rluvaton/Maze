package Game;

import Helpers.Coordinate;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Helpers.Utils;
import Maze.Candy.CandyPowerType;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.GenerateCandyConfig;
import Maze.MazeGenerator.IntegerConfiguration;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import player.ComputerPlayer.ComputerPlayer;

public class GameStep {

    public final static BuiltinStep VERY_EASY = new BuiltinStep(getVeryEasyGameStep(), GameStepLevel.VERY_EASY);
    public final static BuiltinStep EASY = new BuiltinStep(getEasyGameStep(), GameStepLevel.EASY);
    public final static BuiltinStep MEDIUM = new BuiltinStep(getMediumGameStep(), GameStepLevel.MEDIUM);
    public final static BuiltinStep HARD = new BuiltinStep(getHardGameStep(), GameStepLevel.HARD);
    public final static BuiltinStep VERY_HARD = new BuiltinStep(getVeryHardGameStep(), GameStepLevel.VERY_HARD);

    public static BuiltinStep[] STEPS = new BuiltinStep[]{VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD};

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
        return new GameStep()

                .setEdge(new IntegerConfiguration(4))

                .setMinDistance(new IntegerConfiguration(0))
                .setEntrancesCount(new IntegerConfiguration(1))
                .setExitsCount(new IntegerConfiguration(1))

                .setWithComputerPlayer(false)
                .setComputerPlayerSpeed(null);
    }

    private static GameStep getEasyGameStep() {
        return new GameStep()

                .setEdge(new IntegerConfiguration(5, 8))

                .setMinDistance(new IntegerConfiguration(0, 3))
                .setEntrancesCount(new IntegerConfiguration(1))
                .setExitsCount(new IntegerConfiguration(1, 3))

                .setWithComputerPlayer(false)
                .setComputerPlayerSpeed(null);
    }

    private static GameStep getMediumGameStep() {

        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(1, 200))
                .setTypes(new CandyPowerType[]{CandyPowerType.Points})
                .setTimeToLive(new IntegerConfiguration(-1));

        return new GameStep()
                .setEdge(new IntegerConfiguration(10, 15))

                .setMinDistance(new IntegerConfiguration(5, 10))
                .setEntrancesCount(new IntegerConfiguration(1))
                .setExitsCount(new IntegerConfiguration(1))

                .setWithComputerPlayer(false)
                .setComputerPlayerSpeed(null)

                .setTotalCandies(new IntegerConfiguration(4, 7))
                .setCandyConfig(candyConfig);
    }

    private static GameStep getHardGameStep() {
        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(-200, 200))
                .setTypes(new CandyPowerType[]{CandyPowerType.Points, CandyPowerType.Time});

        return new GameStep()

                .setEdge(new IntegerConfiguration(15, 20))

                .setMinDistance(new IntegerConfiguration(15))
                .setEntrancesCount(new IntegerConfiguration(1))
                .setExitsCount(new IntegerConfiguration(1))

                .setWithComputerPlayer(true)
                .setComputerPlayerSpeed(new IntegerConfiguration(500))

                .setTotalCandies(new IntegerConfiguration(10, 15))
                .setCandyConfig(candyConfig);
    }

    private static GameStep getVeryHardGameStep() {
        IntegerConfiguration edge = new IntegerConfiguration(25, 30);

        GenerateCandyConfig candyConfig = new GenerateCandyConfig()
                .setStrengthPower(new IntegerConfiguration(-200, 200))
                .setTypes(CandyPowerType.values())
                .setTimeToLive(new IntegerConfiguration(10000, 35000));

        return new GameStep()

                .setHeight(edge)
                .setWidth(edge)

                .setMinDistance(new IntegerConfiguration(5, 10))
                .setEntrancesCount(new IntegerConfiguration(1))
                .setExitsCount(new IntegerConfiguration(1))

                .setWithComputerPlayer(true)
                .setComputerPlayerSpeed(new IntegerConfiguration(200, 500))

                .setTotalCandies(new IntegerConfiguration(10, 15))
                .setCandyConfig(candyConfig);
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

    /**
     * For Rectangle Maze
     */
    private GameStep setEdge(IntegerConfiguration edge) {
        return this.setHeight(edge)
                .setWidth(edge);
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

    public MazeGame.Builder build() {
        return new MazeGame.Builder()
                .setMazeGenerator(buildMazeGenerator())
                .addSinglePlayer(getPlayer());
    }

    private MazeGenerator buildMazeGenerator() {
        return new MazeGenerator(new RectangleMazeBuilder(), new BFSSolverAdapter())
                .generateMaze(this.height.getValue(), this.width.getValue())
                .createRandomEntrancesAndExists(this.entrancesCount.getValue(), this.exitsCount.getValue(), this.getMinDistance().getValue())
                .generateRandomCandies(Utils.clone(this.candyConfig), totalCandies.getValue());
    }

    public ComputerPlayer getPlayer() {
        if (!isWithComputerPlayer()) {
            return null;
        }

        return new ComputerPlayer(new Coordinate(0, 0), this.computerPlayerSpeed.getValue());
    }

    public static class BuiltinStep {
        private GameStep step;
        private GameStepLevel name;

        public BuiltinStep(GameStep step, GameStepLevel name) {
            ObjectAssertion.requireNonNull(step, "Step can't be null");
            ObjectAssertion.requireNonNull(name, "Step name can't be null");

            this.step = step;
            this.name = name;
        }

        public GameStep getStep() {
            return step;
        }

        public GameStepLevel getName() {
            return name;
        }

        @Override
        public String toString() {
            return name.toString();
        }
    }
}
