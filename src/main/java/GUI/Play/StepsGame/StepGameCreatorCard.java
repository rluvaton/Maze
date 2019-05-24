package GUI.Play.StepsGame;

import GUI.Play.CustomGame.*;
import GUI.Play.CustomGame.Exceptions.NotFinishedStepException;
import GUI.Play.Shared.PlayerCreation.PlayersCreationPanel;
import GUI.Program;
import GUI.Utils.GuiHelper;
import GUI.WindowCard;
import Game.MazeGame;
import Helpers.CallbackFns;
import Helpers.ThrowableAssertions.ObjectAssertion;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class StepGameCreatorCard extends JPanel implements WindowCard {

    private BorderLayout layout;

    private JPanel stepPanel;
    private JProgressBar stepsProgress;
    private JButton nextButton;
    private CardLayout cardLayout;

    // region Steps

    private StepSelectionCard stepSelectionCard;
    private PlayersCreationPanel playersCreationStep;

    // endregion

    private int stepIndex = 0;
    private IPlayConfigStep[] steps = new IPlayConfigStep[2];

    MazeGame.Builder builder = new MazeGame.Builder();

    private CallbackFns.ArgsVoidCallbackFunction<MazeGame.Builder> onBuildFn;

    public StepGameCreatorCard(CallbackFns.ArgsVoidCallbackFunction<MazeGame.Builder> onBuildFn) {
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

//        this.setMinimumSize(new Dimension(100, 100));
//        this.setPreferredSize(new Dimension(100, 100));

        Program.pack();

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

        this.onBuildFn.run(this.builder);

        resetGameConfiguration();
    }

    private void resetGameConfiguration() {
        stepIndex = 0;

        for (IPlayConfigStep step : steps) {
            step.reset();
        }

        builder = new MazeGame.Builder();

        updateSteps();
    }

    private void initSteps() {
        initStepSelectionStep();
        initPlayersCreationStep();

        this.initStepsArr();
    }

    private void initStepSelectionStep() {
        stepSelectionCard = new StepSelectionCard();
        initMazeStep(stepSelectionCard);
    }

    private void initPlayersCreationStep() {
        playersCreationStep = new PlayersCreationPanel();
        initMazeStep(playersCreationStep);
    }

    private void initStepsArr() {
        this.steps[0] = this.stepSelectionCard;
        this.steps[1] = this.playersCreationStep;

        // TODO - ADD all the steps
    }

    private <T extends JPanel & IPlayConfigStep> void initMazeStep(T step) {
        step.init();

        this.addCard(step);
        step.initUIComponents();
    }

    private <T extends JPanel & IPlayConfigStep> void addCard(T step) {
        stepPanel.add(step, step.getPlayStep().getValue());
    }

    public void start() {
        this.showCard(PlayStep.SELECT_MAZE_SHAPE);
    }

    private void showCard(PlayStep playStep) {
        this.cardLayout.show(this.stepPanel, playStep.getValue());

        Optional<Component> currentCard = GuiHelper.getCurrentCard(this.stepPanel);
        currentCard.ifPresent(this::resizeToFitCard);
    }

    private void resizeToFitCard(Component currentCard) {
//        Dimension cardSize = currentCard.getMinimumSize();
//        this.stepPanel.setMinimumSize(cardSize);
//        this.stepPanel.setPreferredSize(null);
//
//        Dimension size = cardLayout.preferredLayoutSize(this.stepPanel);
//
//        currentCard.setPreferredSize(size);
//
//        this.stepPanel.setPreferredSize(size);
//        this.stepPanel.setMinimumSize(size);
//
//        // Hack for sizing the container panel at better size
//        this.setPreferredSize(this.getPreferredSize());

        Program.pack();
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
