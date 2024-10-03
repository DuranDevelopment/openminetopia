package nl.openminetopia.modules.staff.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player source = event.getPlayer();
        OnlineMinetopiaPlayer minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(source);
        if (minetopiaPlayer == null) return;

        if (!source.hasPermission("openminetopia.staffchat") || !minetopiaPlayer.isStaffchatEnabled()) return;

        event.setCancelled(true);

        List<Player> recipients = new ArrayList<>();

        Bukkit.getServer().getOnlinePlayers().forEach(target -> {
            if (target.hasPermission("openminetopia.staffchat")) recipients.add(target);
        });

        // Iterate over recipients
        recipients.forEach(player -> {
            // Send the formatted message to the player
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_gray>[<gold><b>Staff</b><dark_gray>] <dark_gray>(<red><b>" + player.getWorld().getName() + "</b><dark_gray>) <green>" + source.getName() + "<white>: " + ChatUtils.stripMiniMessage(event.message())));
        });
    }
}
