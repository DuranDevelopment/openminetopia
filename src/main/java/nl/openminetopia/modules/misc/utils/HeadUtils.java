package nl.openminetopia.modules.misc.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.DefaultConfiguration;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class HeadUtils {

    public boolean isValidHeadItem(ItemStack head) {

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        for (String headItemString : configuration.getHeadWhitelist()) {
            String[] headItem = headItemString.split(":");
            if (head.getType().name().equalsIgnoreCase(headItem[0])) {
                if (headItem.length == 1) {
                    return true;
                }

                if (head.getItemMeta().getCustomModelData() == Integer.parseInt(headItem[1])) {
                    return true;
                }
            }
        }

        return false;
    }
}
