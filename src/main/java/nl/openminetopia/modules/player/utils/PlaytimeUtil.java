package nl.openminetopia.modules.player.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import nl.openminetopia.configuration.MessageConfiguration;

@UtilityClass
public class PlaytimeUtil {

    public static Component formatPlaytime(int playtimeInSeconds) {
        int days = playtimeInSeconds / 86400;
        int hours = (playtimeInSeconds % 86400) / 3600;
        int minutes = ((playtimeInSeconds % 86400) % 3600) / 60;
        int seconds = ((playtimeInSeconds % 86400) % 3600) % 60;
        // TODO: Replace <days> <hours> <minutes> <seconds> with actual values.
        return MessageConfiguration.component("player_time_format");
    }
}
