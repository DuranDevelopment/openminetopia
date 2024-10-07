package nl.openminetopia.modules.police.handcuff.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.police.handcuff.HandcuffManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void playerMove(final PlayerMoveEvent event) {
        if (!HandcuffManager.getInstance().isHandcuffed(event.getPlayer())) return;

        if (!OpenMinetopia.getDefaultConfiguration().isHandcuffCanRunAway()) event.setCancelled(true);
    }
}
