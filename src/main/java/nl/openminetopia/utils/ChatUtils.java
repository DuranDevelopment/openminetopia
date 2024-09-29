package nl.openminetopia.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.utils.string.ReplaceableString;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@UtilityClass
public class ChatUtils {

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component format(MinetopiaPlayer minetopiaPlayer, String message) {
        Player player = minetopiaPlayer.getBukkit().getPlayer();
        if (player == null) return Component.empty();

        ReplaceableString replaceable = ReplaceableString.of(message)
                .replace("<level_color>", minetopiaPlayer.getActiveColor(OwnableColorType.LEVEL).getColor())
                .replace("<level>", minetopiaPlayer.getLevel() + "")
                .replace("<prefix_color>", minetopiaPlayer.getActiveColor(OwnableColorType.PREFIX).getColor())
                .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                .replace("<name_color>", minetopiaPlayer.getActiveColor(OwnableColorType.NAME).getColor())
                .replace("<name>", player.getName())
                .replace("<chat_color>", minetopiaPlayer.getActiveColor(OwnableColorType.CHAT).getColor())
                .replace("<world_title>", minetopiaPlayer.getWorld().getTitle())
                .replace("<world_loadingname>", minetopiaPlayer.getWorld().getLoadingName())
                .replace("<world_name>", minetopiaPlayer.getWorld().getName())
                .replace("<world_color>", minetopiaPlayer.getWorld().getColor())
                .replace("<city_title>", minetopiaPlayer.getPlace().getTitle()) // Defaults to world name if not in a city
                .replace("<city_loadingname>", minetopiaPlayer.getPlace().getLoadingName()) // Defaults to world loading name
                .replace("<city_name>", minetopiaPlayer.getPlace().getName()) // Defaults to world name
                .replace("<temperature>", minetopiaPlayer.getPlace().getTemperature() + "") // Defaults to world temperature
                .replace("<city_color>", minetopiaPlayer.getPlace().getColor()) // Defaults to world color
                .replace("<date>", new SimpleDateFormat("dd-MM-yyyy").format(new Date()))
                .replace("<time>", new SimpleDateFormat("HH:mm").format(new Date()));

        if (minetopiaPlayer.getFitness().getStatistics() != null && !minetopiaPlayer.getFitness().getStatistics().isEmpty()) {
            replaceable
                    .replace("<fitness>", minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL).getFitnessGained() + "")
                    .replace("<max_fitness>", OpenMinetopia.getDefaultConfiguration().getMaxFitnessLevel() + "");
        }

        return MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(minetopiaPlayer.getBukkit(), replaceable.get()));
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
