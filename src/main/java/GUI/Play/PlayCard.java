package GUI.Play;

import GUI.GameWindow;
import GUI.GuiHelper;
import GUI.MazeGame.MazePanel;
import GUI.Play.Exceptions.NotFinishedStepException;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import player.BasePlayer;
import player.HumanPlayer.HumanPlayer;

import javax.swing.*;
import java.awt.*;

public class PlayCard extends JPanel {

    private JPanel stepPanel;
    private JProgressBar stepsProgress;
    private JButton nextButton;
    private CardLayout cardLayout;

    // region Steps

    private SelectShapeStep selectMazeShape;
    private SelectExitEntranceMinDistanceStep selectExitEntranceMinDistanceStep;
    private CandiesStep candiesStep;
    private CreatePlayersStep createPlayersStep;

    // endregion

    private int stepIndex = 0;
    private IPlayConfigStep[] steps = new IPlayConfigStep[4];

    MazeGenerator.Builder builder = new MazeGenerator.Builder()
            .setSolverAdapter(new BFSSolverAdapter());


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

            if (currentStep == null || !currentStep.canContinue()) {
                System.out.println("Please Finish Step before");
                return;
            }

            try {
                builder = currentStep.appendData(builder);
            } catch (NotFinishedStepException ex) {
                // Shouldn't happen because check before id can continue
                ex.printStackTrace();
            }

            stepIndex++;

            if (this.stepIndex >= this.steps.length) {
                this.onFinish();
                return;
            }

            updateSteps();
        });
    }

    private void updateSteps() {
        this.setProgressData((int) (((double) this.stepIndex / (double) this.steps.length) * 100.0));

        this.showCard(this.steps[stepIndex].getPlayStep());
    }

    private void onFinish() {
        // TODO - FINISH THIS
        // TODO - BUILD THE MAZE

        Maze maze;

        try {
            maze = this.builder.build();
        } catch (MazeBuilderException e) {
            e.printStackTrace();
            return;
        }

        BasePlayer[] players = this.createPlayersStep.playerList.toArray(new HumanPlayer[0]);

        MazePanel mazePanel = new MazePanel(maze, players, false);
        GameWindow.main(mazePanel);

//        throw new UnsupportedOperationException();
    }

    private void initSteps() {
        initSelectMazeShape();
        initSelectExitEntranceMinDistanceStep();
        initCandiesSelectionStep();
        this.initCreatePlayerStep();

        this.initStepsArr();
    }

    private void initCreatePlayerStep() {
        createPlayersStep = new CreatePlayersStep();
        initMazeStep(createPlayersStep);
    }

    private void initCandiesSelectionStep() {
        candiesStep = new CandiesStep();
        initMazeStep(candiesStep);
    }

    private void initStepsArr() {
        this.steps[0] = this.selectMazeShape;
        this.steps[1] = this.selectExitEntranceMinDistanceStep;
        this.steps[2] = this.candiesStep;
        this.steps[3] = this.createPlayersStep;

        // TODO - ADD all the steps
    }

    private void initSelectMazeShape() {
        this.selectMazeShape = new SelectShapeStep();
        initMazeStep(this.selectMazeShape);
    }

    private <T extends JPanel & IPlayConfigStep> void initMazeStep(T step) {
        step.init();

        this.addCard(step);
        step.initComponents();
    }

    private void initSelectExitEntranceMinDistanceStep() {
        this.selectExitEntranceMinDistanceStep = new SelectExitEntranceMinDistanceStep(this.builder);

        initMazeStep(this.selectExitEntranceMinDistanceStep);

    }

    private <T extends JPanel & IPlayConfigStep> void addCard(T step) {
        stepPanel.add(step, step.getPlayStep().getValue());
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

    public boolean back() {
        if (this.stepIndex == 0) {
            return false;
        }

        this.stepIndex--;
        updateSteps();
        return true;
    }
}
