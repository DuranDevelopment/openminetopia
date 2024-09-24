package nl.openminetopia.modules.vehicles.objects;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import nl.openminetopia.modules.vehicles.enums.VehicleKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.ArmorStand;
import org.joml.Vector3f;

import java.util.UUID;

@Getter
public class Seat {

    private final Vehicle vehicle;
    private final Vector3f relativePosition;
    private final boolean isDriver;

    private final ArmorStand entity;
    private final Entity internalEntity;

    public Seat(Vehicle vehicle, Vector3f relativePosition, boolean isDriver) {
        this.vehicle = vehicle;
        this.relativePosition = relativePosition;
        this.isDriver = isDriver;

        this.entity = vehicle.location().getWorld().spawn(vehicle.location(), ArmorStand.class); /* Temp */
        this.internalEntity = ((CraftEntity)entity).getHandle();

        entity.setInvisible(true);
    }

    public void tick() {
        internalEntity.moveTo(globalLocation(), vehicle.degrees(), 0);
    }

    private Vec3 globalLocation() {
        double angle = vehicle.radians();
        double xOffset = relativePosition.x() * Math.cos(angle) - relativePosition.z() * Math.sin(angle);
        double zOffset = relativePosition.x() * Math.sin(angle) + relativePosition.z() * Math.cos(angle);

        Location location = vehicle.location();
        location.add(xOffset, relativePosition.y(), zOffset);

        return CraftLocation.toVec3D(location);
    }

}
