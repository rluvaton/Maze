package GUI.Play.CustomGame;

import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Utils.GuiHelper;
import Game.MazeGame;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;

import javax.swing.*;
import java.awt.*;

public class SelectShapeStep extends JPanel implements IPlayConfigStep {

    private GridLayout layout;
    private JComboBox<String> selectMazeShapeComboBox;
    private JSpinner heightValue;
    private JSpinner widthValue;

    public SelectShapeStep() {
    }

    public void init() {
        this.layout = new GridLayout(3, 2, 6, 6);
        this.setLayout(layout);
    }

    public void initComponents() {
        initSelectMazeShape();

        initWidth();
        initHeight();
    }

    private void initWidth() {
        widthValue = new JSpinner(GuiHelper.createSpinnerModelForPositiveNumberOnly());

        final JLabel label = new JLabel();
        label.setText("Width");

        addInputAndLabelPair(widthValue, label);
    }

    private void initHeight() {
        heightValue = new JSpinner(GuiHelper.createSpinnerModelForPositiveNumberOnly());

        final JLabel label = new JLabel();
        label.setText("Height");

        addInputAndLabelPair(heightValue, label);
    }

    private void initSelectMazeShape() {

        selectMazeShapeComboBox = new JComboBox<>();
        final DefaultComboBoxModel<String> defaultComboBoxModel1 = new DefaultComboBoxModel<>();

        defaultComboBoxModel1.addElement("Rectangular");
        selectMazeShapeComboBox.setModel(defaultComboBoxModel1);

        final JLabel label = new JLabel();
        label.setText("Select Maze Shape");

        label.setLabelFor(selectMazeShapeComboBox);

        addInputAndLabelPair(selectMazeShapeComboBox, label);
    }

    private void addInputAndLabelPair(JComponent input, JLabel label) {
        this.add(label);
        this.add(input);
    }

    @Override
    public boolean canContinue() {
        // TODO - in the future fix this for not getting only rectangular
        return this.selectMazeShapeComboBox.getSelectedItem() != null &&
                ((Integer) this.heightValue.getValue() > 0) &&
                ((Integer) this.widthValue.getValue() > 0);
    }

    @Override
    public PlayStep getPlayStep() {
        return PlayStep.SELECT_MAZE_SHAPE;
    }

    @Override
    public MazeGame.Builder appendData(MazeGame.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        MazeGenerator.Builder mazeGeneratorBuilder = new MazeGenerator.Builder()
                .setSolverAdapter(new BFSSolverAdapter());

        if (this.selectMazeShapeComboBox.getSelectedItem() == "Rectangular") {
            mazeGeneratorBuilder.setMazeBuilder(new RectangleMazeBuilder());
        }

        mazeGeneratorBuilder
                .setHeight((Integer) this.heightValue.getValue())
                .setWidth((Integer) this.widthValue.getValue());

        if (builder == null) {
            builder = new MazeGame.Builder();
        }

        return builder.setMazeGeneratorBuilder(mazeGeneratorBuilder);
    }

    @Override
    public void reset() {
        selectMazeShapeComboBox.setSelectedIndex(0);

        heightValue.setValue(0);
        widthValue.setValue(0);
    }
}
