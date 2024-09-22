package nl.openminetopia.modules.player.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PlaytimeUtil {

    public static String formatPlaytime(int playtimeInSeconds) {
        int days = playtimeInSeconds / 86400;
        int hours = (playtimeInSeconds % 86400) / 3600;
        int minutes = ((playtimeInSeconds % 86400) % 3600) / 60;
        int seconds = ((playtimeInSeconds % 86400) % 3600) % 60;
        return "<aqua>" + days + " <dark_aqua>dagen, <aqua>" + hours + " <dark_aqua>uren, <aqua>"+ minutes + " <dark_aqua>minuten, <aqua>" + seconds + " <dark_aqua>seconden.";
    }
}
