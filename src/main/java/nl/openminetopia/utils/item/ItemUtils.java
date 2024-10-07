package nl.openminetopia.utils.item;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@UtilityClass
public class ItemUtils {

    public boolean isValidItem(ItemStack item, List<String> validItems) {
        for (String headItemString : validItems) {
            String[] headItem = headItemString.split(":");
            if (item.getType().name().equalsIgnoreCase(headItem[0])) {
                if (headItem.length == 1) {
                    return true;
                }

                if (item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == Integer.parseInt(headItem[1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
