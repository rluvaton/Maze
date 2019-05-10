package Helpers;

import java.util.concurrent.TimeUnit;

public class TimeFormatter {
    public static String convertFromSecondsToHumanReadableText(long durationInSeconds) {
        long days = TimeUnit.SECONDS.toDays(durationInSeconds);
        durationInSeconds -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS.toHours(durationInSeconds);
        durationInSeconds -= TimeUnit.HOURS.toSeconds(hours);

        long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds);
        durationInSeconds -= TimeUnit.MINUTES.toSeconds(minutes);

        long seconds = TimeUnit.SECONDS.toSeconds(durationInSeconds);

        StringBuilder readable = new StringBuilder();
        if (days != 0) {
            readable.append(days).append(" day(s)");
        }
        if (hours != 0) {
            readable.append(hours).append(" hours(s)");
        }
        if (minutes != 0) {
            readable.append(minutes).append(" minutes(s)");
        }
        if (seconds != 0) {
            readable.append(seconds).append(" seconds(s)");
        }

        return readable.toString();
    }
}
