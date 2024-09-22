package nl.openminetopia.modules.vehicles.enums;

import nl.openminetopia.OpenMinetopia;
import org.bukkit.NamespacedKey;

public enum VehicleKey {
    TYPE_KEY("vehicle.type"),
    UUIDS_KEY("vehicle.uuids");

    private final NamespacedKey key;

    VehicleKey(String key) {
        this.key = new NamespacedKey(OpenMinetopia.getInstance(), key);
    }

    public NamespacedKey key() {
        return key;
    }

}
