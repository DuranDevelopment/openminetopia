package nl.openminetopia.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlayerChatListener implements Listener {

    private final DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {

        Player source = event.getPlayer();
        List<Player> recipients = new ArrayList<>();

        event.setCancelled(true);

        Bukkit.getServer().getOnlinePlayers().forEach(target -> {
            if (target.getWorld().equals(source.getWorld())
                    && source.getLocation().distance(target.getLocation()) <= configuration.getChatRadiusRange())
                recipients.add(target);
        });

        recipients.remove(source);
        if (recipients.isEmpty() && configuration.isNotifyWhenNobodyInRange()) {
            event.getPlayer().sendMessage(ChatUtils.color("<red>Er zijn geen spelers in de buurt om je bericht te horen."));
            return;
        }

        recipients.add(source);
        OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(source);
        if (minetopiaPlayer == null) return;

        // Format
        String format = configuration.getChatFormat()
                .replace("<levelcolor>", "<white>")
                .replace("<level>", minetopiaPlayer.getLevel() + "")
                .replace("<prefixcolor>", "<white>")
                .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                .replace("<namecolor>", "<white>")
                .replace("<name>", source.getName())
                .replace("<chatcolor>", "<white>")
                .replace("<message>", ChatUtils.stripMiniMessage(event.message()));

        recipients.forEach(player -> player.sendMessage(ChatUtils.color(format)));
    }
}
