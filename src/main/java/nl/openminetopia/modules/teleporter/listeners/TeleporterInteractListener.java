package nl.openminetopia.modules.teleporter.listeners;

import nl.openminetopia.modules.teleporter.utils.TeleporterUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TeleporterInteractListener implements Listener {

    @EventHandler
    public void pressPlate(final PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL)) return;

        Block block = event.getClickedBlock();
        if (!TeleporterUtil.isTeleporterBlock(block)) return;

        Location location = TeleporterUtil.blockLocation(block);
        if (location == null) return;

        event.getPlayer().teleport(location);
    }

}
