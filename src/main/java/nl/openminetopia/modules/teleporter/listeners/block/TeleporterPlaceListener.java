package nl.openminetopia.modules.teleporter.listeners.block;

import com.jazzkuh.inventorylib.utils.PersistentData;
import nl.openminetopia.modules.teleporter.utils.TeleporterUtil;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class TeleporterPlaceListener implements Listener {

    @EventHandler
    public void blockPlace(final BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!TeleporterUtil.isTeleporterItem(item)) return;

        Location location = TeleporterUtil.decodeNBT(item);
        boolean addDisplay = Boolean.TRUE.equals(PersistentData.getBoolean(item, "teleporter.display"));
        if (location == null) return;

        TeleporterUtil.setTeleporter(event.getBlockPlaced(), location, addDisplay);
        event.getPlayer().sendMessage(ChatUtils.color("<gold>Teleporter has been placed."));
    }

}
