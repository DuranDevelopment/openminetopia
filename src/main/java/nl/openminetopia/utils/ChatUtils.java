package nl.openminetopia.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import org.bukkit.entity.Player;

import java.util.Objects;

@UtilityClass
public class ChatUtils {

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component format(MinetopiaPlayer minetopiaPlayer, String message) {
        Player player = minetopiaPlayer.getBukkit().getPlayer();
        if (player == null) return Component.empty();
        message = PlaceholderAPI.setPlaceholders(minetopiaPlayer.getBukkit(), message
                .replace("<fitness>", minetopiaPlayer.getFitness().getTotalFitness() + "")
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
                .replace("<world_color>", minetopiaPlayer.getWorld().getColor()))
                .replace("<city_title>", minetopiaPlayer.getPlace().getTitle()) // Defaults to the world name if the player is not in a city
                .replace("<city_loadingname>", minetopiaPlayer.getPlace().getLoadingName()) // Defaults to the world loading name if the player is not in a city
                .replace("<city_name>", minetopiaPlayer.getPlace().getName()) // Defaults to the world name if the player is not in a city
                .replace("<city_color>", minetopiaPlayer.getPlace().getColor()) // Defaults to the world color if the player is not in a city
                .replace("<temperature>", minetopiaPlayer.getPlace().getTemperature() + "")
                .replace("<max_fitness>", OpenMinetopia.getDefaultConfiguration().getMaxFitnessLevel() + "");
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
