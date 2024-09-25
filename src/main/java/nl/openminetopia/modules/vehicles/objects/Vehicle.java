package nl.openminetopia.modules.vehicles.objects;

import lombok.Getter;
import net.minecraft.world.entity.Entity;
import nl.openminetopia.modules.vehicles.objects.movement.CarMovement;
import nl.openminetopia.modules.vehicles.objects.movement.Movement;
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
    private final Movement movement;

    private final List<Seat> seats = new ArrayList<>();
    private final List<Part> parts = new ArrayList<>();

    public Vehicle(Location location) {
        this.entity = location.getWorld().spawn(location, ArmorStand.class);
        this.internalEntity = ((CraftEntity)entity).getHandle();
        this.movement = CarMovement.movement(this);

        entity.setInvisible(true);
        entity.setInvulnerable(true);
        entity.setAI(false);
    }

    public void tick(WrappedPlayerInputPacket packet) {
        movement.move(packet);
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
