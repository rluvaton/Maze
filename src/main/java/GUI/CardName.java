package GUI;

public enum CardName {
    WELCOME("WelcomeCard"),
    STATS("StatsCard"),
    GAME_CREATOR("GameCreatorCard"),
    CUSTOM_GAME_CREATOR("CustomGameCard"),
    STEPS_GAME_CREATOR("StepsGameCreator"),
    PLAY("PlayCard"),
    GAME("Game");

    private String cardName;

    CardName(String cardName) {
        this.cardName = cardName;
    }

    public String getValue() {
        return cardName;
    }
}
