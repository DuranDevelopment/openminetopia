package nl.openminetopia.modules.police.balaclava.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class BalaclavaUtils {

    public void obfuscate(Player player, boolean obfuscate) {
        if (obfuscate) {
            player.displayName(ChatUtils.color("<obf>Balaclava</obf><reset>"));
            return;
        }
        player.displayName(ChatUtils.color(player.getName()));
    }

    public boolean isBalaclavaItem(ItemStack item) {
        List<ItemStack> balaclavaItems = new ArrayList<>();

        for (String balaclavaItemString : OpenMinetopia.getDefaultConfiguration().getBalaclavaItems()) {
            String itemName = balaclavaItemString.split(":")[0];

            int custommodeldata = -1;

            if (balaclavaItemString.split(":").length == 2) {
                String custommodeldataString = balaclavaItemString.split(":")[1];

                try {
                    custommodeldata = Integer.parseInt(custommodeldataString);
                } catch (NumberFormatException e) {
                    OpenMinetopia.getInstance().getLogger().warning("Invalid custom model data: " + custommodeldataString);
                    continue;
                }
            }

            Material material = Material.matchMaterial(itemName);
            if (material == null) {
                OpenMinetopia.getInstance().getLogger().warning("Invalid material: " + material + " for balaclava item.");
                continue;
            }

            ItemStack balaclavaItem = new ItemBuilder(material).toItemStack();
            if (custommodeldata != -1)
                balaclavaItem = new ItemBuilder(material).setCustomModelData(custommodeldata).toItemStack();

            balaclavaItems.add(balaclavaItem);
        }
        return balaclavaItems.contains(item);
    }
}
