package Statistics;

public class GameStats {
    private int totalGames;
    private int totalSecondsPlayed;
    private User[] users;

    public GameStats(int totalGames, int totalSecondsPlayed, User[] users) {
        this.totalGames = totalGames;
        this.totalSecondsPlayed = totalSecondsPlayed;
        this.users = users;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalSecondsPlayed() {
        return totalSecondsPlayed;
    }

    public User[] getUsers() {
        return users;
    }
}
