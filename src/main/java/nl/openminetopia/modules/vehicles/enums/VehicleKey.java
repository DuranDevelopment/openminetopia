package nl.openminetopia.modules.vehicles.enums;

import nl.openminetopia.OpenMinetopia;
import org.bukkit.NamespacedKey;

public enum VehicleKey {
    DRIVER_KEY("vehicle.driver"),
    RELATIVE_POSITION_KEY("vehicle.relativePosition"),
    SEAT_UUIDS_KEY("vehicle.seatUuids"),
    PART_UUIDS_KEY("vehicle.partUuids"),
    VEHICLE_ID_KEY("vehicle.vehicleId"),
    BASE_VEHICLE_KEY("vehicle.baseVehicle"),
    PART_IDENTIFIER_KEY("vehicle.partId");

    private final NamespacedKey key;

    VehicleKey(String key) {
        this.key = new NamespacedKey(OpenMinetopia.getInstance(), key);
    }

    public NamespacedKey key() {
        return key;
    }

}
