package Maze.MazeGenerator;

import Helpers.CallbackFns.NoArgsCallbackFunction;


import java.util.Comparator;
import java.util.PriorityQueue;

public class MazeGeneratorActions {

    private PriorityQueue<MazeGenerationStep> actions = new PriorityQueue<>(Comparator.comparingInt(o -> o.stepType.priority));

    public MazeGenerator parseActions() {
        MazeGenerator mazeGenerator = null;
        MazeGenerationStep action = null;

        while (!this.actions.isEmpty()) {
            action = this.actions.poll();
            mazeGenerator = action.action.run();
        }

        return mazeGenerator;
    }

    public void add(MazeGenerationStepTypes stepType, NoArgsCallbackFunction<MazeGenerator> action) {
        this.actions.add(new MazeGenerationStep(stepType, action));
    }

    protected class MazeGenerationStep {
        private MazeGenerationStepTypes stepType;
        private NoArgsCallbackFunction<MazeGenerator> action;

        public MazeGenerationStep(MazeGenerationStepTypes stepType, NoArgsCallbackFunction<MazeGenerator> action) {
            this.action = action;
            this.stepType = stepType;
        }
    }

    protected enum MazeGenerationStepTypes {
        CREATE_SKELETON(0),
        GENERATE_MAZE(1),
        CREATE_ELOCATION(2),
        CREATE_CANDIES(3),
        BUILD(4);

        private int priority;

        MazeGenerationStepTypes(int priority) {
            this.priority = priority;
        }

        public int getValue() {
            return priority;
        }
    }
}
