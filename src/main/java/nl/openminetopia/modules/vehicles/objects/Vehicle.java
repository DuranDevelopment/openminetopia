package nl.openminetopia.modules.vehicles.objects;

import lombok.Getter;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private final ArmorStand entity;

    @Getter
    private final List<Seat> seats = new ArrayList<>();

    public Vehicle(Location location) {
        this.entity = location.getWorld().spawn(location, ArmorStand.class);
    }

    public Vehicle(ArmorStand entity) {
        this.entity = entity;
    }

    public void tick(WrappedPlayerInputPacket packet) {
        Vector vector = new Vector(0,0,0);
        if (packet.isForward()) vector.setX(1);
        else if (packet.isBackward()) vector.setX(-1);

        if (packet.isLeft()) vector.setZ(1);
        else if (packet.isRight()) vector.setZ(-1);

        entity.setVelocity(vector);
        seats.forEach(Seat::tick);
    }

    public Seat seat(Vector3f relativePosition) {
        Seat seat = new Seat(this, relativePosition, true);
        seats.add(seat);

        return seat;
    }

    public Location location() {
        return entity.getLocation();
    }

    public double degrees() {
        return Math.toDegrees(entity.getBodyYaw());
    }

}
