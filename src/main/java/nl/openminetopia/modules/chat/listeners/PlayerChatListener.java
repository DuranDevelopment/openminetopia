package nl.openminetopia.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.renderer((player, displayName, message, viewer) -> {

            OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);

            if (minetopiaPlayer == null) return message;

            // Format
            // <dark_gray>[<levelcolor>Level <level><dark_gray>] <dark_gray>[<prefixcolor><prefix><dark_gray>] <namecolor><name>: <chatcolor><message>
            Component level = ChatUtils.color("<gray>Level " + minetopiaPlayer.getLevel());
            Component prefix = ChatUtils.color(" <aqua>" + minetopiaPlayer.getActivePrefix().getPrefix());
            Component name = ChatUtils.color(" <white>" + player.getName() + ": ");
            return Component.empty().append(level).append(prefix).append(name).append(message);
        });
    }
}
