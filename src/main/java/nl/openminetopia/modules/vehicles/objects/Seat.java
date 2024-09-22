package nl.openminetopia.modules.vehicles.objects;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import nl.openminetopia.modules.vehicles.enums.VehicleKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.ArmorStand;
import org.joml.Vector3f;

import java.util.UUID;

public class Seat {

    private final Vehicle vehicle;
    @Getter
    private final ArmorStand entity;
    private final boolean isDriver;

    private final Vector3f relativePosition;

    public Seat(Vehicle vehicle, Vector3f relativePosition, boolean isDriver) {
        this.vehicle = vehicle;
        this.entity = vehicle.location().getWorld().spawn(vehicle.location(), ArmorStand.class); /* Temp */
        this.relativePosition = relativePosition;
        this.isDriver = isDriver;
    }

    public void tick() {
        ((CraftEntity)entity).getHandle().setPos(CraftLocation.toVec3D(globalLocation()));
    }

    private Location globalLocation() {
        double angle = vehicle.degrees();
        double xOffset = relativePosition.x() * Math.cos(angle) - relativePosition.z() * Math.sin(angle);
        double zOffset = relativePosition.x() * Math.sin(angle) + relativePosition.z() * Math.cos(angle);

        Location location = vehicle.location();
        location.add(xOffset, relativePosition.y(), zOffset);

        return location;
    }

}
