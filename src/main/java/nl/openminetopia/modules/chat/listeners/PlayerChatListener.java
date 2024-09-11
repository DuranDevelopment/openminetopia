package nl.openminetopia.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.manager.PlayerManager;
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
            Component level = ChatUtils.color("<gray>" + minetopiaPlayer.getLevel());
            Component finalMessage = level.append(Component.text(player.getName())).append(ChatUtils.color(":"));
            return finalMessage;
        });
    }
}
