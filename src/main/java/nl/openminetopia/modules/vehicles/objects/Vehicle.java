package nl.openminetopia.modules.vehicles.objects;

import com.jeff_media.morepersistentdatatypes.DataType;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.modules.vehicles.configuration.BlueprintManager;
import nl.openminetopia.modules.vehicles.entity.BaseVehicleEntity;
import nl.openminetopia.modules.vehicles.enums.VehicleKey;
import nl.openminetopia.modules.vehicles.objects.movement.CarMovement;
import nl.openminetopia.modules.vehicles.objects.movement.Movement;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.*;

@Getter
public class Vehicle {

    private final BlueprintManager.Blueprint blueprint;
    private final ArmorStand entity;
    private final BaseVehicleEntity internalEntity;
    private final Movement movement;

    private final List<Seat> seats = new ArrayList<>();
    private final List<Part> parts = new ArrayList<>();

    public Vehicle(Location location, BlueprintManager.Blueprint blueprint) {
        this.internalEntity = new BaseVehicleEntity(this, location.getWorld());
        this.entity = (ArmorStand) internalEntity.getBukkitEntity();
        this.movement = CarMovement.inst(this);
        this.blueprint = blueprint;
        VehiclesModule.vehicles.add(this);

        Optional.ofNullable(entity.getAttribute(Attribute.GENERIC_STEP_HEIGHT)).ifPresent(attribute -> {
            attribute.setBaseValue(0.5);
        });

        internalEntity.spawn(location);
    }

    public Vehicle(BaseVehicleEntity internalEntity) {
        this.internalEntity = internalEntity;
        this.entity = (ArmorStand) internalEntity.getBukkitEntity();
        this.movement = CarMovement.inst(this);
        this.blueprint = null;
        VehiclesModule.vehicles.add(this);
    }

    public void movementTick(WrappedPlayerInputPacket packet) {
        movement.move(packet);
    }

    public void tick() {
        seats.forEach(Seat::tick);
        parts.forEach(Part::tick);
    }

    public Seat seat(Vector3f relativePosition, boolean isDriver) {
        Seat seat = new Seat(this, relativePosition, isDriver);
        seats.add(seat);

        update();
        return seat;
    }

    public Part part(String identifier) {
        Part part = new Part(this, identifier);
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

    public void extractSeats() {
        String[] entities = entity.getPersistentDataContainer().get(VehicleKey.SEAT_UUIDS_KEY.key(), DataType.STRING_ARRAY);
        if (entities == null) return;

        for (String s : entities) {
            ArmorStand seatEntity = (ArmorStand) entity.getWorld().getEntity(UUID.fromString(s));
            if (seatEntity == null) continue;

            seats.add(new Seat(this, seatEntity));
        }
    }

    public void extractParts() {
        String[] entities = entity.getPersistentDataContainer().get(VehicleKey.PART_UUIDS_KEY.key(), DataType.STRING_ARRAY);
        if (entities == null) return;

        for (String s : entities) {
            ItemDisplay partEntity = (ItemDisplay) entity.getWorld().getEntity(UUID.fromString(s));
            if (partEntity == null) return;

            parts.add(new Part(this, partEntity));
        }
    }

}
