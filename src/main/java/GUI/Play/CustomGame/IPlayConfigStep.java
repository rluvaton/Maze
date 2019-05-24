package GUI.Play.CustomGame;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import Game.MazeGame;
import Maze.MazeGenerator.MazeGenerator;

public interface IPlayConfigStep {

    void init();

    void initUIComponents();

    void reset();

    boolean canContinue();

    default void onNextStep(MazeGame.Builder builder) {
    }

    PlayStep getPlayStep();

    MazeGame.Builder appendData(MazeGame.Builder builder) throws NotFinishedStepException;
}
