package nl.openminetopia.modules.chat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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

        // Format the message
        String originalMessage = ChatUtils.stripMiniMessage(event.message());
        String formattedMessage = configuration.getChatFormat()
                .replace("<levelcolor>", "<white>")
                .replace("<level>", minetopiaPlayer.getLevel() + "")
                .replace("<prefixcolor>", minetopiaPlayer.getActivePrefixColor().getColor())
                .replace("<prefix>", minetopiaPlayer.getActivePrefix().getPrefix())
                .replace("<namecolor>", "<white>")
                .replace("<name>", source.getName())
                .replace("<chatcolor>", "<white>");

// Iterate over recipients
        recipients.forEach(player -> {
            // Replace <message> placeholder with original message
            String finalMessage = formattedMessage.replace("<message>", originalMessage);

            // Check if the player's name is in the original message and highlight it
            if (originalMessage.contains(player.getName())) {
                String highlightedMessage = originalMessage.replace(player.getName(), "<green>" + player.getName() + "<white>");
                finalMessage = formattedMessage.replace("<message>", highlightedMessage);

                // Play sound for the mentioned player
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            }

            // Send the formatted message to the player
            player.sendMessage(ChatUtils.color(finalMessage));
        });
    }
}
