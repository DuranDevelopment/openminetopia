package nl.openminetopia.modules.misc.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.utils.item.ItemUtils;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class HeadUtils {

    public boolean isValidHeadItem(ItemStack head) {
        return ItemUtils.isValidItem(head, OpenMinetopia.getDefaultConfiguration().getHeadWhitelist());
    }
}
