package nl.openminetopia.modules.vehicles.objects;

import com.jazzkuh.inventorylib.utils.PersistentData;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
        entity.setInvulnerable(true);
        entity.setGravity(false);

        save();
    }

    public Seat(Vehicle vehicle, ArmorStand entity) {
        this.vehicle = vehicle;
        this.entity = entity;
        this.internalEntity = ((CraftEntity)entity).getHandle();

        PersistentDataContainer data = entity.getPersistentDataContainer();
        int[] posData = data.get(VehicleKey.RELATIVE_POSITION_KEY.key(), PersistentDataType.INTEGER_ARRAY);
        this.isDriver = Boolean.TRUE.equals(data.get(VehicleKey.DRIVER_KEY.key(), PersistentDataType.BOOLEAN));

        if (posData == null) posData = new int[3];
        this.relativePosition = new Vector3f(posData[0], posData[1], posData[2]);
    }

    public void tick() {
        internalEntity.setDeltaMovement(vehicle.getInternalEntity().getDeltaMovement());
        internalEntity.moveTo(globalLocation(), vehicle.degrees(), 0);
    }

    private void save() {
        PersistentDataContainer data = entity.getPersistentDataContainer();

        data.set(VehicleKey.RELATIVE_POSITION_KEY.key(), PersistentDataType.INTEGER_ARRAY,
                new int[]{(int) relativePosition.x(), (int) relativePosition.y(), (int) relativePosition.z()});

        data.set(VehicleKey.DRIVER_KEY.key(), PersistentDataType.BOOLEAN, isDriver);
    }

    private Vec3 globalLocation() {
        double angle = vehicle.radians();
        double xOffset = relativePosition.x() * Math.cos(angle) - relativePosition.z() * Math.sin(angle);
        double zOffset = relativePosition.x() * Math.sin(angle) + relativePosition.z() * Math.cos(angle);

        Location location = vehicle.location();
        location.add(xOffset, relativePosition.y(), zOffset);

        return CraftLocation.toVec3D(location);
    }

    public String serializableUuid() {
        return entity.getUniqueId().toString();
    }

}
