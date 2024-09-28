package nl.openminetopia.modules.vehicles.objects;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import nl.openminetopia.modules.vehicles.enums.VehicleKey;
import nl.openminetopia.modules.vehicles.objects.movement.BoatMovement;
import nl.openminetopia.modules.vehicles.objects.movement.CarMovement;
import nl.openminetopia.modules.vehicles.objects.movement.Movement;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        this.movement = CarMovement.inst(this);

        entity.setInvisible(true);
        entity.setInvulnerable(true);
        entity.setAI(false);
    }

    public Vehicle(ArmorStand entity) {
        this.entity = entity;
        this.internalEntity = ((CraftEntity)entity).getHandle();
        this.movement = CarMovement.inst(this);

        this.extractSeats();
        this.extractParts();
    }

    public void tick(WrappedPlayerInputPacket packet) {
        movement.move(packet);
        seats.forEach(Seat::tick);
        parts.forEach(Part::tick);
    }

    public Seat seat(Vector3f relativePosition, boolean isDriver) {
        Seat seat = new Seat(this, relativePosition, isDriver);
        seats.add(seat);

        update();
        return seat;
    }

    public Part part() {
        Part part = new Part(this);
        parts.add(part);

        update();
        return part;
    }

    public void update() {
        PersistentDataContainer data = entity.getPersistentDataContainer();

        data.set(VehicleKey.SEAT_UUIDS_KEY.key(), DataType.STRING_ARRAY,
                seats.stream().map(Seat::serializableUuid).toArray(String[]::new));

        data.set(VehicleKey.PART_UUIDS_KEY.key(), DataType.STRING_ARRAY,
                parts.stream().map(Part::serializableUuid).toArray(String[]::new));
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

    private void extractSeats() {
        String[] entities = entity.getPersistentDataContainer().get(VehicleKey.SEAT_UUIDS_KEY.key(), DataType.STRING_ARRAY);
        if (entities == null) return;

        for (String s : entities) {
            ArmorStand seatEntity = (ArmorStand) Bukkit.getEntity(UUID.fromString(s));
            if (seatEntity == null) continue;

            seats.add(new Seat(this, seatEntity));
        }
    }

    private void extractParts() {
        String[] entities = entity.getPersistentDataContainer().get(VehicleKey.PART_UUIDS_KEY.key(), DataType.STRING_ARRAY);
        if (entities == null) return;

        for (String s : entities) {
            ItemDisplay partEntity = (ItemDisplay) Bukkit.getEntity(UUID.fromString(s));
            if (partEntity == null) return;

            parts.add(new Part(this, partEntity));
        }
    }

}
