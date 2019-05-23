package Maze.MazeGenerator;

import Helpers.Coordinate;
import Helpers.SuccessCloneable;
import Helpers.Utils;
import Maze.Candy.CandyPowerType;

import java.util.concurrent.TimeUnit;

public class GenerateCandyConfig implements SuccessCloneable<GenerateCandyConfig> {

    public static final IntegerConfiguration NO_TIME = new IntegerConfiguration(-1);

    public static IntegerConfiguration DEFAULT_TIME_TO_LIVE = new IntegerConfiguration(
            (int) TimeUnit.SECONDS.toMillis(20),
            (int) TimeUnit.MINUTES.toMillis(5)
    );

    public static IntegerConfiguration DEFAULT_STRENGTH_POWER = new IntegerConfiguration(-1000, 1000);

    private CandyPowerType[] types = CandyPowerType.values();
    private IntegerConfiguration timeToLive = DEFAULT_TIME_TO_LIVE;
    private IntegerConfiguration strengthPower = DEFAULT_STRENGTH_POWER;
    private Coordinate otherCellLocation = null;
    private Coordinate cellLoc = null;

    public GenerateCandyConfig() {
    }

    public CandyPowerType[] getTypes() {
        return types;
    }

    public GenerateCandyConfig setTypes(CandyPowerType[] types) {
        if (types != null) {
            this.types = types;
        }
        return this;
    }

    public IntegerConfiguration getTimeToLive() {
        return timeToLive;
    }

    public GenerateCandyConfig setTimeToLive(IntegerConfiguration timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    public IntegerConfiguration getStrengthPower() {
        return strengthPower;
    }

    public GenerateCandyConfig setStrengthPower(IntegerConfiguration strengthPower) {
        this.strengthPower = strengthPower;
        return this;
    }

    public Coordinate getOtherCellLocation() {
        return otherCellLocation;
    }

    public GenerateCandyConfig setOtherCellLocation(Coordinate otherCellLocation) {
        this.otherCellLocation = otherCellLocation;
        return this;
    }

    public Coordinate getCellLoc() {
        return cellLoc;
    }

    public GenerateCandyConfig setCellLoc(Coordinate cellLoc) {
        this.cellLoc = cellLoc;
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public GenerateCandyConfig clone() {
        return new GenerateCandyConfig()
                .setTimeToLive(Utils.clone(timeToLive))
                .setStrengthPower(Utils.clone(strengthPower))
                .setOtherCellLocation(Utils.clone(otherCellLocation))
                .setCellLoc(Utils.clone(cellLoc))
                .setTypes(types.clone());
    }
}
