package nl.openminetopia.modules.vehicles.objects.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Pig;

import java.lang.reflect.InvocationTargetException;

public abstract class Movement {

    public final Vehicle vehicle;
    public final ArmorStand entity;
    public final Entity internalEntity;

    public Movement(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.entity = vehicle.getEntity();
        this.internalEntity = vehicle.getInternalEntity();
    }

    public abstract void move(WrappedPlayerInputPacket packet);

}
