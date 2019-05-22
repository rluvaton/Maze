package Maze.MazeGenerator;

import Helpers.RandomHelper;
import Helpers.SuccessCloneable;

public class IntegerConfiguration implements SuccessCloneable<IntegerConfiguration> {
    private int defaultValue;
    private int from;
    private int to;
    private boolean randomize;

    public IntegerConfiguration(int defaultValue) {
        this.defaultValue = defaultValue;
        this.randomize = false;
    }

    public IntegerConfiguration(int from, int to) {
        this.from = from;
        this.to = to;
        this.randomize = true;
    }

    public int getValue() {
        return randomize ? RandomHelper.getRandomNumber(from, to) : defaultValue;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    public IntegerConfiguration clone() {
        return randomize ? new IntegerConfiguration(from, to) : new IntegerConfiguration(defaultValue);
    }
}
