package GUI.Play.CustomGame;

import GUI.MazeGame.MazePanel;
import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Utils.GuiHelper;
import GUI.WindowCard;
import Game.MazeGame;
import Helpers.Builder.BuilderException;
import Helpers.CallbackFns;
import Helpers.ThrowableAssertions.ObjectAssertion;
import Maze.Maze;
import Maze.MazeBuilder.Exceptions.MazeBuilderException;
import Maze.MazeGenerator.MazeGenerator;
import Maze.Solver.BFS.BFSSolverAdapter;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;

public class PlayCard extends JPanel implements WindowCard {

    private BorderLayout layout;

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

    MazeGame.Builder builder = new MazeGame.Builder();

    private CallbackFns.ArgsVoidCallbackFunction<MazePanel> onBuildFn;


    public PlayCard(CallbackFns.ArgsVoidCallbackFunction<MazePanel> onBuildFn) {
        ObjectAssertion.requireNonNull(onBuildFn, "`onBuildFn` can't be null");
        this.onBuildFn = onBuildFn;
    }

    public void init() {
        this.layout = new BorderLayout();
        this.setLayout(layout);
    }

    public void initComponents() {

        CellConstraints cc = new CellConstraints();

//        createHeader(cc);
        setProgressBar(cc);
        initStepContainer(cc);
        initNextStepBtn();

        this.setMinimumSize(new Dimension(100, 100));
        this.setPreferredSize(new Dimension(100, 100));

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

            currentStep.onNextStep(builder);

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

        MazeGame game;

        try {
            game = this.builder.build();
        } catch (BuilderException e) {
            e.printStackTrace();
            return;
        }

        MazePanel mazePanel = new MazePanel(game);

        this.onBuildFn.run(mazePanel);

        resetGameConfiguration();
    }

    private void resetGameConfiguration() {
        stepIndex = 0;

        for (IPlayConfigStep step : steps) {
            step.reset();
        }

        builder = new MazeGame.Builder();

        selectExitEntranceMinDistanceStep.setBuilder(builder);

        updateSteps();
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

        Component currentCard = GuiHelper.findCurrentComponent(this.stepPanel);
        if(currentCard != null) {
            Dimension cardSize = currentCard.getMinimumSize();
            this.stepPanel.setMinimumSize(cardSize);
            this.stepPanel.setPreferredSize(null);
        }

        this.setPreferredSize(null);
    }

    private void initNextStepBtn() {
        nextButton = new JButton();
        nextButton.setText("Next");

        this.add(nextButton, BorderLayout.PAGE_END);
    }

    private void initStepContainer(CellConstraints cc) {
        stepPanel = new JPanel();

        cardLayout = new CardLayout(0, 0);
        stepPanel.setLayout(cardLayout);

        this.add(stepPanel, BorderLayout.CENTER);
    }

    private void createHeader(CellConstraints cc) {
        final JLabel cardLabel = new JLabel();
        Font label1Font = GuiHelper.getFont("Source Code Pro", Font.BOLD, 18, cardLabel.getFont());
        if (label1Font != null) {
            cardLabel.setFont(label1Font);
        }
        cardLabel.setText("Play");
    }

    private void setProgressBar(CellConstraints cc) {
        stepsProgress = new JProgressBar();
        stepsProgress.setString("0%");
        stepsProgress.setStringPainted(true);
        stepsProgress.setValue(0);

        this.add(stepsProgress, BorderLayout.PAGE_START);
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
