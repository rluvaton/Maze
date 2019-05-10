package GUI.Play;

import GUI.CardName;
import GUI.GuiHelper;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

public class PlayCard extends JPanel {

    private JPanel stepPanel;
    private JProgressBar stepsProgress;
    private JButton nextButton;
    private CardLayout cardLayout;

    // region Steps

    private SelectShapeStep selectMazeShape;

    // endregion

    private int stepIndex = 0;
    private IPlayConfigStep[] steps = new IPlayConfigStep[6];


    public PlayCard() {
    }

    public void init() {
        this.setLayout(new FormLayout("fill:296px:grow", "center:38px:noGrow,top:11dlu:noGrow,center:106px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
    }

    public void initComponents() {

        CellConstraints cc = new CellConstraints();

        createHeader(cc);
        initStepContainer(cc);
        initNextStepBtn();
        setProgressBar(cc);

        initSteps();

        initListeners();
    }

    private void initListeners() {
        initNextStepClickListener();
    }

    private void initNextStepClickListener() {
        nextButton.addActionListener(e -> {
            IPlayConfigStep currentStep = this.steps[stepIndex];

            if(currentStep == null || !currentStep.canContinue()) {
                System.out.println("Please Finish Step before");
                return;
            }

            stepIndex++;

            if(this.stepIndex >= this.steps.length) {
                this.onFinish();
                return;
            }

            this.showCard(this.steps[stepIndex].getPlayStep());
        });
    }

    private void onFinish() {
        // TODO - FINISH THIS
        throw new UnsupportedOperationException();
    }

    private void initSteps() {
        initSelectMazeShape();

        this.initStepsArr();
    }

    private void initStepsArr() {
        this.steps[0] = this.selectMazeShape;

        // TODO - ADD other steps
    }

    private void initSelectMazeShape() {
        this.selectMazeShape = new SelectShapeStep();
        selectMazeShape.init();

        stepPanel.add(selectMazeShape, PlayStep.SELECT_MAZE_SHAPE.getValue());
        selectMazeShape.initComponents();
    }

    public void start() {
        this.showCard(PlayStep.SELECT_MAZE_SHAPE);
    }

    private void showCard(PlayStep playStep) {
        this.cardLayout.show(this.stepPanel, playStep.getValue());
    }

    private void initNextStepBtn() {
        nextButton = new JButton();
        nextButton.setText("Next");
        this.add(nextButton, new CellConstraints(1, 5, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets(0, 40, 0, 40)));
    }

    private void initStepContainer(CellConstraints cc) {
        stepPanel = new JPanel();

        cardLayout = new CardLayout(0, 0);
        stepPanel.setLayout(cardLayout);
        this.add(stepPanel, cc.xy(1, 3));
    }

    private void createHeader(CellConstraints cc) {
        final JLabel label1 = new JLabel();
        Font label1Font = GuiHelper.getFont("Source Code Pro", Font.BOLD, 18, label1.getFont());
        if (label1Font != null) {
            label1.setFont(label1Font);
        }
        label1.setText("Play");
        this.add(label1, cc.xy(1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
    }

    private void setProgressBar(CellConstraints cc) {
        stepsProgress = new JProgressBar();
        stepsProgress.setString("0%");
        stepsProgress.setStringPainted(true);
        stepsProgress.setValue(0);
        this.add(stepsProgress, cc.xy(1, 2));
    }

    private void setProgressData(int progressData) {
        stepsProgress.setString(progressData + "%");
        stepsProgress.setValue(progressData);
    }
}
