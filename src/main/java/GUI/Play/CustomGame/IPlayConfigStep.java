package GUI.Play.CustomGame;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import Maze.MazeGenerator.MazeGenerator;

public interface IPlayConfigStep {

    void init();

    void initComponents();

    boolean canContinue();

    default void onNextStep() {
    }

    PlayStep getPlayStep();

    MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException;
}
