package Statistics;

public class User {
    private String name;

    private int totalGames = 0;
    private int totalTimeLimitedGames = 0;
    private int maxMazeAreaPlayed = 0;
    private int totalTimePlayedInSeconds = 0;
    private double winningPercentage = 0;

    public User(Builder builder) {
        this.name = builder.name;
        this.totalGames = builder.totalGames;
        this.totalTimeLimitedGames = builder.totalTimeLimitedGames;
        this.maxMazeAreaPlayed = builder.maxMazeAreaPlayed;
        this.totalTimePlayedInSeconds  = builder.totalTimePlayedInSeconds;
        this.winningPercentage  = builder.winningPercentage;
    }

    public String getName() {
        return name;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalTimeLimitedGames() {
        return totalTimeLimitedGames;
    }

    public int getMaxMazeAreaPlayed() {
        return maxMazeAreaPlayed;
    }

    public int getTotalTimePlayedInSeconds() {
        return totalTimePlayedInSeconds;
    }

    public double getWinningPercentage() {
        return winningPercentage;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class Builder {
        private String name;
        private int totalGames = 0;
        private int totalTimeLimitedGames = 0;
        private int maxMazeAreaPlayed = 0;
        private int totalTimePlayedInSeconds = 0;
        private double winningPercentage = 0;

        public Builder() {
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setTotalGames(int totalGames) {
            this.totalGames = totalGames;
            return this;
        }

        public Builder setTotalTimeLimitedGames(int totalTimeLimitedGames) {
            this.totalTimeLimitedGames = totalTimeLimitedGames;
            return this;
        }

        public Builder setMaxMazeAreaPlayed(int maxMazeAreaPlayed) {
            this.maxMazeAreaPlayed = maxMazeAreaPlayed;
            return this;
        }

        public Builder setTotalTimePlayedInSeconds(int totalTimePlayedInSeconds) {
            this.totalTimePlayedInSeconds = totalTimePlayedInSeconds;
            return this;
        }

        public Builder setWinningPercentage(double winningPercentage) {
            this.winningPercentage = winningPercentage;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
