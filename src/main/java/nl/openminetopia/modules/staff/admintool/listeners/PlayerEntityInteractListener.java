package nl.openminetopia.modules.staff.admintool.listeners;

import nl.openminetopia.modules.staff.admintool.menus.AdminToolMenu;
import nl.openminetopia.utils.PersistentDataUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEntityInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (!(event.getRightClicked() instanceof Player target)) return;
        if (item.getType() != Material.NETHER_STAR) return;
        if (PersistentDataUtil.get(item, "openmt.admintool") == null) return;

        AdminToolMenu adminMenu = new AdminToolMenu(event.getPlayer(), target);
        adminMenu.open(event.getPlayer());
    }
}
