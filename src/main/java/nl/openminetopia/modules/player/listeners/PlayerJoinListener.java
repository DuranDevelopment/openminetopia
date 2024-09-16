package nl.openminetopia.modules.player.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.kick(ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            return;
        }
        minetopiaPlayer.load();
        minetopiaPlayer.getFitnessRunnable().runTaskTimer(OpenMinetopia.getInstance(),0,60 * 20L);
    }
}