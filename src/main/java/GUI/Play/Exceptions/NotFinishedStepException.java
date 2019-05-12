package GUI.Play.Exceptions;

import GUI.Play.IPlayConfigStep;

public class NotFinishedStepException extends Exception {

    private IPlayConfigStep step;

    public NotFinishedStepException(IPlayConfigStep step) {
        this.step = step;
    }
    public NotFinishedStepException(String message, IPlayConfigStep step) {
        super(message);
        this.step = step;
    }
}
