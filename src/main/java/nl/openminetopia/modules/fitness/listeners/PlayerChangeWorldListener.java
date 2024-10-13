package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.api.places.MTWorldManager;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorldListener implements Listener {

    @EventHandler
    public void worldChange(final PlayerChangedWorldEvent event) {
        if (MTWorldManager.getInstance().getWorld(event.getFrom().getSpawnLocation()) != null) return;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(event.getPlayer());
        if (minetopiaPlayer == null) return;
        minetopiaPlayer.getFitness().getRunnable().run();
    }
}
