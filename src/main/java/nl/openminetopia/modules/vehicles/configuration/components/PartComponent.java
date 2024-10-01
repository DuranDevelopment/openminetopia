package nl.openminetopia.modules.vehicles.configuration.components;

import org.bukkit.Material;

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
                            String hexColor, int customModelData, boolean visible) {}
