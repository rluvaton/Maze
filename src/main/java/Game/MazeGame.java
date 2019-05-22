package Game;

import GUI.MazeGame.ControlledTimer;
import Helpers.*;
import Helpers.Builder.BuilderException;
import Helpers.Builder.IBuilder;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Logger.LoggerManager;
import Maze.Candy.CandyRecord;
import Maze.Cell;
import Maze.ELocation;
import Maze.ELocationType;
import Maze.Maze;
import Maze.MazeGenerator.MazeGenerator;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import player.BasePlayer;
import player.ComputerPlayer.ComputerPlayer;
import player.HumanPlayer.HumanPlayer;
import player.MoveStatus;
import player.exceptions.PlayerNotRunning;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Logger.LoggerManager.logger;

public class MazeGame {

    /**
     * Maze
     */
    private Maze maze;

    /**
     * Maze Players
     */
    private List<BasePlayer> players;

    /**
     * Timers for all the expired candies
     * (have time to live value bigger than zero)
     */
    private List<ControlledTimer> timeToLiveCandiesTimers;

    private MovementListenerManager movementListenerManager;

    /**
     * List of all players callback for starting the game
     */
    private ArrayList<CallbackFns.NoArgsVoidCallbackFunction> startPlayersCallbacks;

    /**
     * Subject to stop all the active subscriptions
     */
    private final Subject onDestroySub = PublishSubject.create();


    private GameState gameState = GameState.NOT_READY;


    // region Create MazeGame from Step

    public static MazeGame createMazeGameFromStep(GameStep step) throws BuilderException {
        return MazeGame.createMazeGameFromStep(step, Collections.EMPTY_LIST, false);
    }

    public static MazeGame createMazeGameFromStep(GameStep step, List<BasePlayer> players) throws BuilderException {
        return MazeGame.createMazeGameFromStep(step, players, false);
    }

    public static MazeGame createMazeGameFromStep(GameStep step, List<BasePlayer> players, boolean atEntrances) throws BuilderException {
        ObjectAssertion.requireNonNull(step, "Step can't be null");
        ObjectAssertion.requireNonNull(players, "Players can't be null");

        Builder builder = step.build();

        if(!players.isEmpty()) {
            builder.addManyPlayers(players, atEntrances);
        }

        return builder.build();
    }

    // endregion

    public MazeGame(Maze maze, List<BasePlayer> players, MovementListenerManager movementListenerManager) {
        this(maze, players, movementListenerManager, false);
    }

    public MazeGame(Maze maze, BasePlayer[] players, MovementListenerManager movementListenerManager) {
        this(maze, players, movementListenerManager, false);
    }

    public MazeGame(Maze maze, BasePlayer[] players, MovementListenerManager movementListenerManager, boolean arePlayersAtPosition) {
        this(maze, List.of(players), movementListenerManager, arePlayersAtPosition);
    }

    public MazeGame(Maze maze, List<BasePlayer> players, boolean arePlayersAtPosition) {
        this(maze, players, null, arePlayersAtPosition);
    }

    public MazeGame(Maze maze, BasePlayer[] players, boolean arePlayersAtPosition) {
        this(maze, List.of(players), null, arePlayersAtPosition);
    }

    public MazeGame(Maze maze, List<BasePlayer> players, MovementListenerManager movementListenerManager, boolean arePlayersAtPosition) {
        ObjectAssertion.requireNonNull(maze, "Maze can't be null");
        ObjectAssertion.requireNonNull(players, "Players can't be null");
        // The `movementListenerManager` can be null until the game init (can add in start play game)

        this.maze = maze;

        // Clone the players
        this.players = players.stream().filter(Objects::nonNull).collect(Collectors.toList());

        this.movementListenerManager = movementListenerManager;

        if (!arePlayersAtPosition) {
            setAllPlayersLocationAtEntrances();
        }
    }

    private void setAllPlayersLocationAtEntrances() {
        List<Coordinate> locations = maze.getEntrancesLocations();

        if (locations.size() == 0) {
            locations.add(this.getDefaultEntrance());
        }

        int defaultIndex = 0;

        Coordinate defaultEntrance = locations.get(defaultIndex);
        locations.remove(defaultIndex);

        for (BasePlayer player : this.players) {
            setEntranceForSinglePlayer(locations, defaultEntrance, player);
        }
    }

    private void setEntranceForSinglePlayer(List<Coordinate> locations, Coordinate defaultEntrance, BasePlayer player) {
        Coordinate playerLoc;
        playerLoc = defaultEntrance;

        if (!locations.isEmpty()) {
            playerLoc = locations.get(0);
            locations.remove(0);
        }
        player.setLocation(playerLoc);
    }

    public void initGame(MovementListenerManager movementListenerManager) {
        ObjectAssertion.requireNonNull(movementListenerManager, "movementListenerManager can't be null");

        this.movementListenerManager = movementListenerManager;
        this.initGame();
    }

    public void initGame() {
        ObjectAssertion.requireNonNull(movementListenerManager, "movementListenerManager can't be null");

        this.startPlayersCallbacks = initPlayers();

        logger.debug("[Maze Game][Reminder] Don't forget to call `startGame()`");

        gameState = GameState.READY;
    }

    public void startGame() {

        listenToPauseAction();

        runPlayers();

        startCandiesTimers();

        gameState = GameState.RUNNING;
    }

    private void listenToPauseAction() {
        Observable.merge(getAllPlayersPauseObservables())
                .takeUntil(onDestroySub)
                .subscribe(this::onPlayerPauseOrResume);
    }

    private List<Observable<Boolean>> getAllPlayersPauseObservables() {
        ObjectAssertion.requireNonNull(this.players, "Players can't be null");

        return this.players
                .stream()
                .filter(Objects::nonNull)
                .map(BasePlayer::getOnPlayerPauseObs)
                .collect(Collectors.toList());
    }

    private void onPlayerPauseOrResume(Object isPaused) {
        ObjectAssertion.requireNonNull(isPaused, "isPause can't be null and must be a Boolean");

        if (isTheGameCantResumeOrPause()) {
            // Maybe throw an exception here
            return;
        }

        try {
            if ((boolean) isPaused) {
                pauseGame();
            } else {
                resumeGame();
            }
        } catch (PlayerNotRunning playerNotRunning) {
            handlePlayerNotRunningExceptionOnPauseOrResume(playerNotRunning);
        }
    }

    private void handlePlayerNotRunningExceptionOnPauseOrResume(PlayerNotRunning playerNotRunning) {
        playerNotRunning.printStackTrace();
        logger.error("[OnPause][PlayerNotRunning]");
    }

    private void resumeGame() throws PlayerNotRunning {
        this.gameState = GameState.RUNNING;
        BasePlayer.resumeAllPlayers();
        this.resumeAllTimeLimitedCandies();
    }

    private void pauseGame() throws PlayerNotRunning {
        this.gameState = GameState.PAUSE;
        BasePlayer.pauseAllPlayers();
        this.pauseAllTimeLimitedCandies();
    }

    private boolean isTheGameCantResumeOrPause() {
        return this.gameState == GameState.FINISHED || this.gameState == GameState.NOT_READY || this.gameState == GameState.READY;
    }

    private void pauseAllTimeLimitedCandies() {
        timeToLiveCandiesTimers.forEach(ControlledTimer::pause);
    }

    private void resumeAllTimeLimitedCandies() {
        timeToLiveCandiesTimers.forEach(ControlledTimer::resume);
    }

    private void runPlayers() {
        startPlayersCallbacks.forEach(CallbackFns.NoArgsVoidCallbackFunction::run);
    }

    private void startCandiesTimers() {
        timeToLiveCandiesTimers = getAllTimeLimitedCandies();

        timeToLiveCandiesTimers.forEach(ControlledTimer::start);

    }

    private List<ControlledTimer> getAllTimeLimitedCandies() {
        ObjectAssertion.requireNonNull(this.maze, "Maze can't be null");
        return this.maze.getCandies()
                .stream()
                .filter(candyLoc -> candyLoc.candy.getTimeToLive() > 0)
                .map(this::createControlledTimerForTimeLimitedCandy)
                .collect(Collectors.toList());
    }

    private ControlledTimer createControlledTimerForTimeLimitedCandy(CandyRecord candyRecord) {
        ControlledTimer candyTimer = new ControlledTimer(candyRecord.candy.getTimeToLive(), () -> {
            this.maze.getCell(candyRecord.coordinate).removeCandy(candyRecord.candy);
        });

        onDestroySub.subscribe((b) -> {
            candyTimer.cancel();
        });

        return candyTimer;
    }

    private ArrayList<CallbackFns.NoArgsVoidCallbackFunction> initPlayers() {
        ArrayList<CallbackFns.NoArgsVoidCallbackFunction> startPlayersCallbacks = new ArrayList<>();
        CallbackFns.NoArgsVoidCallbackFunction initPlayerCallback;

        for (BasePlayer player : this.players) {
            initPlayerCallback = initSinglePlayer(player);

            if (initPlayerCallback != null) {
                startPlayersCallbacks.add(initPlayerCallback);
            }
        }

        return startPlayersCallbacks;
    }

    private CallbackFns.NoArgsVoidCallbackFunction initSinglePlayer(BasePlayer player) {
        // Create entrance
        ELocation entrance = this.maze.getRandomEntrance();

        // Set default location to 0,0
        player.setLocation(entrance != null ? entrance.getLocation() : new Coordinate(0, 0));

        listenToPlayerMovement(player);

        if (player instanceof HumanPlayer) {
            return this.startHumanPlayer((HumanPlayer) player);
        } else if (player instanceof ComputerPlayer) {
            return startComputerPlayer((ComputerPlayer) player);
        }

        return null;
    }

    private void listenToPlayerMovement(BasePlayer player) {
        player.getPlayerMoveObs()
                .takeUntil(onDestroySub)
                .subscribe(direction -> {
                    logger.debug("[Player][OnMove] - Direction: " + direction);
                    MoveStatus res = this.movePlayer(player, (Direction) direction);
                    switch (res) {
                        case VALID:
                            System.out.println("Time is: " + player.getTime() + " | Points is: " + player.getPoints());
                            break;
                        case INVALID_MOVE:
                            System.out.println("Not Valid Move");
                            break;
                        case FINISHED:
                            System.out.println("User Finished!");
                            this.playerFinished(player);
                            break;
                        default:
                            System.out.println("Unknown move status " + res);
                            break;
                    }
                });
    }

    private CallbackFns.NoArgsVoidCallbackFunction startComputerPlayer(ComputerPlayer player) {
        Thread playerThread = player.createRunningThread(this.maze, getExitForComputerPlayer(player));

        if (playerThread == null) {
            System.out.println("Player " + player.getName() + " can't start running");
            return null;
        }


        return playerThread::start;
    }

    private CallbackFns.NoArgsVoidCallbackFunction startHumanPlayer(HumanPlayer player) {
        logger.debug("[Before Player Start]");

        this.movementListenerManager.addListenerForPlayer(player);
        Thread playerThread = player.create();

        return playerThread::start;
    }

    private Coordinate getExitForComputerPlayer(ComputerPlayer player) {
        return this.maze.getExits().stream().filter(eLocation -> !eLocation.getLocation().equals(player.getLocation())).findFirst().get().getLocation();
    }

    /**
     * Player Finished
     *
     * @param player The player that finished
     */
    private void playerFinished(BasePlayer player) {
        player.onPlayerFinished();

        if (player instanceof HumanPlayer) {
            movementListenerManager.removeListenerForPlayer((HumanPlayer) player);
        }
    }

    /**
     * Move Player Handler
     *
     * @param player    Player that wanted to move
     * @param direction The direction of the move
     * @return Returns the status of the move ${@link MoveStatus}
     * @see #changePlayerLocation(BasePlayer, Coordinate, boolean) Change Player Location - (i.e teleportation)
     */
    private MoveStatus movePlayer(BasePlayer player, Direction direction) {
        ObjectAssertion.requireNonNull(player, "Player can't be null");
        Coordinate loc = player.getLocation();

        Coordinate teleportLocation;

        MoveStatus moved = MoveStatus.INVALID_MOVE;

        if (this.maze.isValidMove(loc, direction)) {
            moved = movePlayerValidMove(player, direction);
        } else if ((teleportLocation = isOnTeleportCandy(loc)) != null) {
            moved = teleportPlayerToLocation(player, teleportLocation);
        }

        // Don't use `player.getLocation()` because it will bring you the new location
        return isLocationAnExit(loc, direction) ? MoveStatus.FINISHED : moved;
    }

    private boolean isLocationAnExit(Coordinate loc, Direction direction) {
        return this.maze.checkIfELocation(loc, direction, ELocationType.Exit) != null;
    }

    private MoveStatus teleportPlayerToLocation(BasePlayer player, Coordinate teleportLocation) {
        MoveStatus moveStatus = MoveStatus.getValidationMoveFromBoolean(this.changePlayerLocation(player, teleportLocation, true));

        LoggerManager.logger.info("[Player][Move] name - " + player.getName());
        return moveStatus;
    }

    private Coordinate isOnTeleportCandy(Coordinate currentCellLocation) {
        Cell cell = this.maze.getCell(currentCellLocation);
        if (cell == null) {
            return null;
        }

        return cell.collectLocationCandyPortal();
    }

    private MoveStatus movePlayerValidMove(BasePlayer player, Direction direction) {
        Cell cell;
        Coordinate teleportLocation;
        player.setLocation(direction);

        cell = this.maze.getCell(player.getLocation());

        if (cell == null) {
            return MoveStatus.INVALID_MOVE;
        }

        // Add the time and points from the candy
        player.addTime(cell.collectTimeCandyStrengths());
        player.addPoints(cell.collectPointsCandyStrengths());

        teleportLocation = cell.collectLocationCandyPortal();

        if (teleportLocation != null) {
            this.changePlayerLocation(player, teleportLocation, true);
        }

        System.out.println("Player " + player.getName() + " Moved");
        return MoveStatus.VALID;
    }

    /**
     * Change Player Location
     *
     * @param player       Player to move
     * @param location     location to move the player to
     * @param isTeleported Is the location changed due to teleportation?
     * @return Returns if the played moved
     * @see #movePlayer(BasePlayer, Direction) For Moving Player by direction of move
     */
    private boolean changePlayerLocation(BasePlayer player, Coordinate location, boolean isTeleported) {

        if (!this.maze.isValidLocation(location)) {
            return false;
        }

        player.setLocation(location);

        Cell cell = this.maze.getCell(player.getLocation());

        // Add the time and points from the candy
        player.addTime(cell.collectTimeCandyStrengths());
        player.addPoints(cell.collectPointsCandyStrengths());

        if (!isTeleported) {
            Coordinate teleportLocation = cell.collectLocationCandyPortal();

            if (teleportLocation != null) {
                this.changePlayerLocation(player, teleportLocation, true);
            }
        }

        if (isTeleported) {
            System.out.println("Teleported");
            try {
                player.onPlayerTeleported();
            } catch (PlayerNotRunning playerNotRunning) {
                playerNotRunning.printStackTrace();
                // TODO - do something
            }
        } else {
            System.out.println("Changed location");
        }

        return true;
    }

    public void onFinishGame() {
        this.onDestroySub.onNext(true);
    }

    public void addPlayer(BasePlayer player) {
        this.addPlayer(player, false);
    }

    public void addPlayer(BasePlayer player, boolean atEntrance) {
        ObjectAssertion.requireNonNull(player, "Player can't be null");

        CallbackFns.NoArgsVoidCallbackFunction initPlayerCallback;

        if (!this.isNewPlayerCanBeAdded()) {
            return;
        }

        if (!atEntrance) {
            setEntranceForLatePlayer(player);
        }

        if (this.isNewPlayerNeedToBeInitialized()) {
            initPlayerCallback = initSinglePlayer(player);

            if (initPlayerCallback != null) {
                startPlayersCallbacks.add(initPlayerCallback);
            }
        }

        this.players.add(player);
    }

    private boolean isNewPlayerNeedToBeInitialized() {
        return this.gameState == GameState.READY;
    }

    private void setEntranceForLatePlayer(BasePlayer player) {
        Coordinate entrance = getUntakenEntrance();

        if (entrance == null) {
            entrance = this.getDefaultEntrance();
        }

        player.setLocation(entrance);
    }

    private Coordinate getDefaultEntrance() {
        return new Coordinate(0, 0);
    }

    private Coordinate getUntakenEntrance() {
        List<Coordinate> locations = this.maze.getEntrancesLocations();

        if (locations.size() == 0) {
            return null;
        }

        List<Coordinate> playersEntranceLocation = this.getAllPlayersEntranceLocations();

        return locations.stream()
                .filter(location -> !playersEntranceLocation.contains(location))
                .findFirst().orElse(null);
    }

    private List<Coordinate> getAllPlayersEntranceLocations() {
        ObjectAssertion.requireNonNull(this.players, "Players can't be null");

        return this.players.stream()
                .map(BasePlayer::getLocation)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean isNewPlayerCanBeAdded() {
        return this.gameState == GameState.READY ||
                this.gameState == GameState.NOT_READY;
    }

    public Maze getMaze() {
        return maze;
    }

    public List<BasePlayer> getPlayers() {
        return players;
    }

    public int getMazeWidth() {
        return this.maze.getWidth();
    }

    public int getMazeHeight() {
        return this.maze.getHeight();
    }

    public GameState getGameState() {
        return gameState;
    }

    public static class Builder implements SuccessCloneable<Builder>, IBuilder<MazeGame> {

        /**
         * 4 types of maze creation
         */
        private MazeGenerator mazeGenerator;
        private MazeGenerator.Builder mazeGeneratorBuilder;

        private Maze maze;
        private GameStep step;

        private List<BasePlayer> players = new LinkedList<>();
        private boolean atEntrances;

        public MazeGenerator getMazeGenerator() {
            return mazeGenerator;
        }

        public Builder setMazeGenerator(MazeGenerator mazeGenerator) {
            this.mazeGenerator = mazeGenerator;

            if (mazeGenerator != null) {
                this.mazeGeneratorBuilder = null;
                this.maze = null;
                this.step = null;
            }
            return this;
        }

        public MazeGenerator.Builder getMazeGeneratorBuilder() {
            return mazeGeneratorBuilder;
        }

        public Builder setMazeGeneratorBuilder(MazeGenerator.Builder mazeGeneratorBuilder) {
            this.mazeGeneratorBuilder = mazeGeneratorBuilder;

            if (mazeGeneratorBuilder != null) {
                this.mazeGenerator = null;
                this.maze = null;
                this.step = null;
            }

            return this;
        }

        public Maze getMaze() {
            return maze;
        }

        public Builder setMaze(Maze maze) {
            this.maze = maze;

            if (this.maze != null) {
                this.mazeGeneratorBuilder = null;
                this.mazeGenerator = null;
                this.step = null;
            }

            return this;
        }

        public GameStep getStep() {
            return step;
        }

        public Builder setStep(GameStep step) {
            this.step = step;

            if (this.step != null) {
                this.mazeGeneratorBuilder = null;
                this.mazeGenerator = null;
                this.maze = null;
            }

            return this;
        }

        public List<BasePlayer> getPlayers() {
            return players;
        }

        // region Players

        public Builder addSinglePlayer(BasePlayer player) {
            return addSinglePlayer(player, false);
        }

        public Builder addSinglePlayer(BasePlayer player, boolean atEntrances) {
            if (this.players == null) {
                List<BasePlayer> players = new LinkedList<>();
                players.add(player);
                return setPlayers(players, atEntrances);
            }

            if (this.atEntrances != atEntrances) {
                this.atEntrances = false;
            }

            this.players.add(player);

            return this;
        }


        public Builder addManyPlayers(List<BasePlayer> players) {
            return addManyPlayers(players, false);
        }

        public Builder addManyPlayers(List<BasePlayer> players, boolean atEntrances) {
            if (this.players == null) {
                return this.setPlayers(players);
            }

            if (this.atEntrances != atEntrances) {
                this.atEntrances = false;
            }

            this.players.addAll(players);

            return this;
        }

        public Builder setPlayers(List<BasePlayer> players) {
            return setPlayers(players, false);
        }

        public Builder setPlayers(List<BasePlayer> players, boolean atEntrances) {
            this.players = players;
            this.atEntrances = atEntrances;
            return this;
        }

        // endregion

        @Override
        public MazeGame build() throws BuilderException {
            if (!this.canBuild()) {
                throw new BuilderException("Maze Game");
            }

            if (this.players != null) {
                if (maze != null) {
                    return new MazeGame(maze, this.players, this.atEntrances);
                } else if (mazeGenerator != null) {
                    return new MazeGame(mazeGenerator.create(), this.players, this.atEntrances);
                } else if (mazeGeneratorBuilder != null) {
                    return new MazeGame(mazeGeneratorBuilder.build(), this.players, this.atEntrances);
                } else {
                    return MazeGame.createMazeGameFromStep(step, this.players, this.atEntrances);
                }
            } else {
                return MazeGame.createMazeGameFromStep(step);
            }
        }

        private boolean canBuild() {
            return ((maze != null || mazeGenerator != null || mazeGeneratorBuilder != null) && players != null) || step != null;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public Builder clone() {
            return new MazeGame.Builder()
                    .setPlayers(players, atEntrances)
                    .setMazeGenerator(mazeGenerator)
                    .setMazeGeneratorBuilder(mazeGeneratorBuilder)
                    .setMaze(maze)
                    .setStep(step);
        }
    }
}
