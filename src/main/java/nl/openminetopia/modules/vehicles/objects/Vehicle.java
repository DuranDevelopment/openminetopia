package nl.openminetopia.modules.vehicles.objects;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Vehicle {

    private final ArmorStand entity;
    private final Entity internalEntity;

    private final List<Seat> seats = new ArrayList<>();
    private final List<Part> parts = new ArrayList<>();

    public Vehicle(Location location) {
        this.entity = location.getWorld().spawn(location, ArmorStand.class);
        this.internalEntity = ((CraftEntity)entity).getHandle();

        entity.setInvisible(true);
    }

    public void tick(WrappedPlayerInputPacket packet) {
        /* Movement is temporary */
        double speed = 0;
        if (packet.isForward()) speed = 1.25;
        else if (packet.isBackward()) speed = -1.25;
        entity.setVelocity(vector(speed));

        if (packet.isLeft()) internalEntity.setRot(internalEntity.yRotO - 5, 0);
        else if (packet.isRight()) internalEntity.setRot(internalEntity.yRotO + 5, 0);

        seats.forEach(Seat::tick);
        parts.forEach(Part::tick);
    }

    public Seat seat(Vector3f relativePosition, boolean isDriver) {
        Seat seat = new Seat(this, relativePosition, isDriver);
        seats.add(seat);

        return seat;
    }

    public Part part() {
        Part part = new Part(this);
        parts.add(part);

        return part;
    }

    private Vector vector(double speed) {
        Vector vector = entity.getLocation().getDirection();
        vector.multiply(speed);
        vector.setY(-1);

        return vector;
    }

    public Location location() {
        return entity.getLocation();
    }

    public float radians() {
        return (float) Math.toRadians(entity.getBodyYaw());
    }

    public float degrees() {
        return entity.getBodyYaw();
    }

}
