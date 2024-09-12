package nl.openminetopia.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.renderer((player, displayName, message, viewer) -> {

            OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);

            if (minetopiaPlayer == null) return message;

            DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

            // Format
            String format = configuration.getChatFormat()
                    .replace("<levelcolor>", "<white>")
                    .replace("<level>", minetopiaPlayer.getLevel() + "")
                    .replace("<prefixcolor>", "<white>")
                    .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                    .replace("<namecolor>", "<white>")
                    .replace("<name>", player.getName())
                    .replace("<chatcolor>", "<white>")
                    .replace("<message>", ChatUtils.stripMiniMessage(message));

            return ChatUtils.color(format);
        });
    }
}
