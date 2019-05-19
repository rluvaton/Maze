package GUI.Play;

import GUI.Play.Exceptions.NotFinishedStepException;
import GUI.Utils.SpringUtilities;
import Maze.MazeBuilder.RectangleMazeBuilder;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;

public class SelectShapeStep extends JPanel implements IPlayConfigStep {

    private SpringLayout layout;
    private JComboBox<String> selectMazeShapeComboBox;
    private JSpinner heightValue;
    private JSpinner widthValue;

    public SelectShapeStep() {
    }

    public void init() {
        this.layout = new SpringLayout();
        this.setLayout(layout);
        setPreferredSize(getPreferredSize());

    }

    public void initComponents() {

        initSelectMazeShape();

        initWidth();
        initHeight();

        SpringUtilities.makeCompactGrid(
                this,
                3, 2,    //rows, cols
                6, 6, //initX, initY
                6, 6);   //xPad, yPa
    }

    private void initWidth() {
        widthValue = new JSpinner();

        final JLabel label = new JLabel();
        label.setText("Width");

        addInputAndLabelPair(widthValue, label);
    }

    private void initHeight() {
        heightValue = new JSpinner();

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

        layout.putConstraint(SpringLayout.WEST, input, 5, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.NORTH, input, 5, SpringLayout.NORTH, this);
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
    public MazeGenerator.Builder appendData(MazeGenerator.Builder builder) throws NotFinishedStepException {
        if (!canContinue()) {
            throw new NotFinishedStepException(this);
        }

        if (builder == null) {
            builder = new MazeGenerator.Builder()
                    .setSolverAdapter(new BFSSolverAdapter());
        }

        if (this.selectMazeShapeComboBox.getSelectedItem() == "Rectangular") {
            builder.setMazeBuilder(new RectangleMazeBuilder());
        }

        return builder
                .setHeight((Integer) this.heightValue.getValue())
                .setWidth((Integer) this.widthValue.getValue());
    }
}
