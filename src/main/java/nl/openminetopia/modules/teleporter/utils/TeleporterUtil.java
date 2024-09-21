package nl.openminetopia.modules.teleporter.utils;

import com.jazzkuh.inventorylib.utils.PersistentData;
import com.jeff_media.customblockdata.CustomBlockData;
import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.teleporter.utils.enums.PressurePlate;
import nl.openminetopia.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

@UtilityClass
public final class TeleporterUtil {

    public ItemStack buildPlate(PressurePlate plate, Location location) {
        ItemBuilder builder = new ItemBuilder(plate.getMaterial());
        builder.setName("<gold>Teleporter Plate");

        builder.addLoreLine("<dark_gray>X: <gray>" + location.x());
        builder.addLoreLine("<dark_gray>Y: <gray>" + location.y());
        builder.addLoreLine("<dark_gray>Z: <gray>" + location.z());
        builder.addLoreLine("<dark_gray>World: <gray>" + location.getWorld().getName());

        /* Should just use MorePersistentDataTypes but okay */
        builder.setNBT("teleporter.x", location.x());
        builder.setNBT("teleporter.y", location.y());
        builder.setNBT("teleporter.z", location.z());
        builder.setNBT("teleporter.yaw", location.getYaw());
        builder.setNBT("teleporter.pitch", location.getPitch());
        builder.setNBT("teleporter.world", location.getWorld().getName());

        return builder.toItemStack();
    }

    public Location decodeNBT(ItemStack item) {
        Double x = PersistentData.getDouble(item, "teleporter.x");
        Double y = PersistentData.getDouble(item, "teleporter.y");
        Double z = PersistentData.getDouble(item, "teleporter.z");
        Float yaw = (Float) PersistentData.get(item, "teleporter.yaw");
        Float pitch = (Float) PersistentData.get(item, "teleporter.pitch");
        String worldName = PersistentData.getString(item, "teleporter.world");

        if (x == null || y == null || z == null || yaw == null || pitch == null || worldName == null) return null;
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    /* Assuming a teleporter check has been done */
    public Location blockLocation(Block block) {
        PersistentDataContainer data = new CustomBlockData(block, OpenMinetopia.getInstance());
        return data.get(new NamespacedKey(OpenMinetopia.getInstance(), "teleporter.location"), DataType.LOCATION);
    }

    public void setTeleporter(Block block, Location location) {
        PersistentDataContainer data = new CustomBlockData(block, OpenMinetopia.getInstance());
        data.set(new NamespacedKey(OpenMinetopia.getInstance(), "teleporter.location"), DataType.LOCATION, location);
    }

    public boolean isTeleporterItem(ItemStack item) {
        return PersistentData.getDouble(item, "teleporter.x") != null;
    }

    public boolean isTeleporterBlock(Block block) {
        PersistentDataContainer data = new CustomBlockData(block, OpenMinetopia.getInstance());
        return data.has(new NamespacedKey(OpenMinetopia.getInstance(), "teleporter.location"));
    }

}
