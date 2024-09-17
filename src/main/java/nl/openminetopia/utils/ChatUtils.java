package nl.openminetopia.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class ChatUtils {

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component format(MinetopiaPlayer minetopiaPlayer, String message) {
        Player player = minetopiaPlayer.getBukkit().getPlayer();
        if (player == null) return Component.empty();
        message = message
                .replace("<fitness>", minetopiaPlayer.getFitness() + "")
                .replace("<levelcolor>", "<white>")
                .replace("<level>", minetopiaPlayer.getLevel() + "")
                .replace("<prefixcolor>", minetopiaPlayer.getActivePrefixColor().getColor())
                .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                .replace("<namecolor>", "<white>")
                .replace("<name>", player.getName())
                .replace("<chatcolor>", "<white>");
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }
}
