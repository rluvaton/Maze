package Statistics;

public class UsersDataSource {

    /**
     * TODO - REPLACE WITH DB OR READ FROM FILE
     */
    private static GameStats demo = createDemo();

    private static GameStats createDemo() {
        int totalGames = 5;
        int totalSecondsPlayed = 100;

        User[] users = {
                new User.Builder()
                        .setName("Test 1")
                        .setMaxMazeAreaPlayed(400)
                        .setTotalGames(2)
                        .setTotalTimeLimitedGames(0)
                        .setTotalTimePlayedInSeconds(30)
                        .setWinningPercentage(43.76)
                        .build(),
                new User.Builder()
                        .setName("Test 2")
                        .setMaxMazeAreaPlayed(700)
                        .setTotalGames(3)
                        .setTotalTimeLimitedGames(1)
                        .setTotalTimePlayedInSeconds(70)
                        .setWinningPercentage(83.5)
                        .build()
        };

        return new GameStats(totalGames, totalSecondsPlayed, users);
    }

    public static GameStats getStats() {
        return demo;
    }
}
