package Maze.MazeGenerator;

import Helpers.Coordinate;
import Maze.Candy.CandyPowerType;

import java.util.concurrent.TimeUnit;

public class GenerateCandyConfig {

    private CandyPowerType[] types = CandyPowerType.values();
    private MazeGenerator.IntegerConfiguration timeToLive = new MazeGenerator.IntegerConfiguration(
            (int) TimeUnit.SECONDS.toMillis(20),
            (int) TimeUnit.MINUTES.toMillis(5)
    );
    private MazeGenerator.IntegerConfiguration strengthPower = new MazeGenerator.IntegerConfiguration(-1000, 1000);
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

    public MazeGenerator.IntegerConfiguration getTimeToLive() {
        return timeToLive;
    }

    public GenerateCandyConfig setTimeToLive(MazeGenerator.IntegerConfiguration timeToLive) {
        this.timeToLive = timeToLive;
        return this;
    }

    public MazeGenerator.IntegerConfiguration getStrengthPower() {
        return strengthPower;
    }

    public GenerateCandyConfig setStrengthPower(MazeGenerator.IntegerConfiguration strengthPower) {
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
}