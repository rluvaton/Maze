package GUI.Play.CustomGame.Exceptions;

import GUI.Play.CustomGame.IPlayConfigStep;

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
