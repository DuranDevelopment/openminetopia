package nl.openminetopia.modules.banking.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.BankingConfiguration;
import nl.openminetopia.modules.banking.menu.BankTypeSelectionMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BankingInteractionListener implements Listener {

    @EventHandler
    public void bankingInteraction(PlayerInteractEvent event) {
        if(!(event.getHand() != EquipmentSlot.HAND)) return;
        if(event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        Material material = block.getType();

        BankingConfiguration bankingConfiguration = OpenMinetopia.getBankingConfiguration();
        if(bankingConfiguration.getAtmMaterial() != material) return;
        new BankTypeSelectionMenu(player).open(player);
    }

}
