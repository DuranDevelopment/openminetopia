package nl.openminetopia.modules.vehicles.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.modules.vehicles.enums.VehicleEntityType;
import nl.openminetopia.modules.vehicles.enums.VehicleKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@UtilityClass
public class VehicleUtils {

    @NotNull
    public VehicleEntityType getType(Entity entity) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        if (!data.has(VehicleKey.TYPE_KEY.key())) return VehicleEntityType.UNKNOWN;

        String type = data.get(VehicleKey.TYPE_KEY.key(), PersistentDataType.STRING).toLowerCase(Locale.ROOT);
        return switch (type) {
            case "main" -> VehicleEntityType.MAIN;
            case "display" -> VehicleEntityType.DISPLAY;
            case "seat" -> VehicleEntityType.SEAT;
            default -> VehicleEntityType.UNKNOWN;
        };
    }

}
