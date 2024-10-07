package nl.openminetopia.modules.police.balaclava.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.police.balaclava.utils.BalaclavaUtils;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerArmorChangeListener implements Listener {

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent event) {
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.HEAD) return;

        if (BalaclavaUtils.isBalaclavaItem(event.getNewItem()) && !BalaclavaUtils.isBalaclavaItem(event.getOldItem())) {
            MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(event.getPlayer());
            if (minetopiaPlayer == null) return;

            BalaclavaUtils.obfuscate(event.getPlayer(), true);
            return;
        }

        if (BalaclavaUtils.isBalaclavaItem(event.getOldItem()) && !BalaclavaUtils.isBalaclavaItem(event.getNewItem())) {
            MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(event.getPlayer());
            if (minetopiaPlayer == null) return;

            BalaclavaUtils.obfuscate(event.getPlayer(), false);
        }
    }
}
