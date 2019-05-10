package GUI.Play;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class SelectShapeStep extends JPanel implements IPlayConfigStep {

    private JComboBox<String> selectMazeShapeComboBox;
    private JSpinner heightValue;
    private JSpinner widthValue;

    public SelectShapeStep() {
    }

    public void init() {
        this.setLayout(new FormLayout("fill:p:noGrow,left:59dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:52px:grow,left:4dlu:noGrow,fill:d:grow,left:5dlu:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
//        stepPanel.add(selectMazeShape, "Card1");
    }

    public void initComponents() {

        CellConstraints cc = new CellConstraints();

        initSelectMazeShape();

        initHeight(cc);

        initWidth(cc);
    }

    private void initWidth(CellConstraints cc) {
        widthValue = new JSpinner();
        this.add(widthValue, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label3 = new JLabel();
        label3.setText("Width");
        this.add(label3, new CellConstraints(1, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initHeight(CellConstraints cc) {
        heightValue = new JSpinner();
        this.add(heightValue, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));

        final JLabel label2 = new JLabel();
        label2.setText("Height");
        this.add(label2, new CellConstraints(1, 3, 3, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    private void initSelectMazeShape() {
        selectMazeShapeComboBox = new JComboBox<>();
        final DefaultComboBoxModel<String> defaultComboBoxModel1 = new DefaultComboBoxModel<String>();

        defaultComboBoxModel1.addElement("Rectangular");
        selectMazeShapeComboBox.setModel(defaultComboBoxModel1);
        this.add(selectMazeShapeComboBox, new CellConstraints(5, 1, 4, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 10)));

        final JLabel label4 = new JLabel();
        label4.setText("Select Maze Shape");
        this.add(label4, new CellConstraints(1, 1, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));

        label4.setLabelFor(selectMazeShapeComboBox);
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
}
