package nl.openminetopia.utils;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
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
                .replace("<levelcolor>", "<white>")
                .replace("<level>", minetopiaPlayer.getLevel() + "")
                .replace("<prefixcolor>", minetopiaPlayer.getActivePrefixColor().getColor())
                .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                .replace("<namecolor>", "<white>")
                .replace("<name>", player.getName())
                .replace("<chatcolor>", "<white>")
                .replace("<world_title>", Objects.requireNonNullElse(minetopiaPlayer.getWorld().getTitle(), "null"))
                .replace("<world_loadingname>", Objects.requireNonNullElse(minetopiaPlayer.getWorld().getLoadingName(), "null"))
                .replace("<world_name>", Objects.requireNonNullElse(minetopiaPlayer.getWorld().getName(), "null"))
                .replace("<world_color>", Objects.requireNonNullElse(minetopiaPlayer.getWorld().getColor(), "null"))
                .replace("<city_title>", Objects.requireNonNullElse(minetopiaPlayer.getPlace().getTitle(), "null")) // Defaults to the world name if the player is not in a city
                .replace("<city_loadingname>", Objects.requireNonNullElse(minetopiaPlayer.getPlace().getLoadingName(), "null")) // Defaults to the world loading name if the player is not in a city
                .replace("<city_name>", Objects.requireNonNullElse(minetopiaPlayer.getPlace().getName(), "null")) // Defaults to the world name if the player is not in a city
                .replace("<city_color>", Objects.requireNonNullElse(minetopiaPlayer.getPlace().getColor(), "null")) // Defaults to the world color if the player is not in a city
                .replace("<temperature>", minetopiaPlayer.getPlace().getTemperature() + "")
                .replace("<max_fitness>", OpenMinetopia.getDefaultConfiguration().getMaxFitnessLevel() + ""));
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
