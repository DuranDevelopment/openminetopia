package nl.openminetopia.modules.vehicles.configuration.components;

import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @param identifier      Identifier in the config.
 * @param displayName     Sets the display name of the part.
 * @param material        Material of part.
 * @param namespacedNBT   NBT for Optifine Optional
 * @param hexColor        Only applies if the Material is colorable  Optional
 * @param customModelData CustomModelData for Vanilla Optional
 * @param visible         Setting if part should be shown.
 */
public record PartComponent(String identifier, String displayName, Material material, String namespacedNBT,
                            String hexColor, int customModelData, boolean visible) {

    public ItemStack item() {
        ItemBuilder builder = new ItemBuilder(material);
        builder.setName(displayName);

        if (namespacedNBT != null) {
            String[] split = namespacedNBT.split(":");
            builder.setNBT(split[0], split[1]);
        }
        if (hexColor != null) builder.setLeatherArmorColor(Color.BLACK);
        if (customModelData != -1) builder.setCustomModelData(customModelData);

        return builder.toItemStack();
    }

}
