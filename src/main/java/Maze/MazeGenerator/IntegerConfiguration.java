package Maze.MazeGenerator;

import Helpers.RandomHelper;

public class IntegerConfiguration {
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
}
