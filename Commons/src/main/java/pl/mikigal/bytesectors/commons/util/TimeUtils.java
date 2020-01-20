package pl.mikigal.bytesectors.commons.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static long[] millisToTime(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return new long[]{days, hours, minutes, seconds};
    }

    public static String millisToText(long millis) {
        long[] converted = millisToTime(millis);
        long days = converted[0];
        long hours = converted[1];
        long minutes = converted[2];
        long seconds = converted[3];

        if (days != 0) {
            return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        }

        if (hours != 0) {
            return hours + "h " + minutes + "m " + seconds + "s";
        }

        if (minutes != 0) {
            return minutes + "m " + seconds + "s";
        }

        return seconds + "s";
    }
}
