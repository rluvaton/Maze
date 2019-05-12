package GUI;

public enum CardName {
    WELCOME("WelcomeCard"),
    STATS("StatsCard"),
    GENERATOR("GeneratorCard"),
    PLAY("PlayCard");

    private String cardName;

    CardName(String cardName) {
        this.cardName = cardName;
    }

    public String getValue() {
        return cardName;
    }
}
