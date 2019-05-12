package GUI.Play;

public enum PlayStep {

    // 1
    SELECT_MAZE_SHAPE("SelectMazeShape&Dimensions"),

    // 2
    SET_EXIT_ENTRANCES_MIN_DISTANCE("SetExit&Entrance&MinDistance"),

    // 3
    CANDIES_SELECTION("CandiesSelection"),

    // 4
    SELECT_OTHER_GAME_FEATURES("SelectOtherGameFeatures"),

    // 5
    CREATE_PLAYERS("CreatePlayers"),

    // 6
    RUN("Run");

    private String stepName;

    PlayStep(String stepName) {
        this.stepName = stepName;
    }

    public String getValue() {
        return stepName;
    }
}
