package nl.openminetopia.modules.places.listeners;

import net.kyori.adventure.title.Title;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        Title title = Title.title(ChatUtils.format(minetopiaPlayer, "Welkom in"), ChatUtils.format(minetopiaPlayer, minetopiaPlayer.getPlace().getLoadingName()));
        player.showTitle(title);
    }
}
