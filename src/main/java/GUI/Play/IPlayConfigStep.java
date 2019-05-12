package GUI.Play;

import GUI.Play.Exceptions.NotFinishedStepException;
import Maze.MazeGenerator.MazeGenerator;

public interface IPlayConfigStep {

    void init();
    void initComponents();

    boolean canContinue();
    PlayStep getPlayStep();

    MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException;
}
