package nl.openminetopia.modules.vehicles.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.objects.movement.CarMovement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BaseVehicleEntity extends ArmorStand {

    private final Vehicle vehicle;
    private int repeatCollision;
    private boolean initialized = false;

    public BaseVehicleEntity(EntityType<? extends Entity> type, Level world) {
        super(EntityType.ARMOR_STAND, world);
        this.vehicle = new Vehicle(this);
    }

    public BaseVehicleEntity(Vehicle vehicle, World world) {
        super(EntityType.ARMOR_STAND, ((CraftWorld)world).getHandle());
        this.vehicle = vehicle;

        setInvulnerable(true);
        setInvisible(true);
    }

    @Override
    public void tick() {
        super.tick();
        vehicle.tick();

        if (this.horizontalCollision) repeatCollision++; else repeatCollision = 0;
        if (repeatCollision > 3) {
            ((CarMovement)vehicle.getMovement()).setSpeed(0);
        }

        if (moonrise$getChunkStatus() == FullChunkStatus.ENTITY_TICKING && !initialized) { /* If chunk has loaded fully, then extract the other entities */
            vehicle.extractSeats();
            vehicle.extractParts();

            this.initialized = true;
        }
    }

    @Override
    public boolean save(CompoundTag nbt) {
        boolean result = super.save(nbt);
        nbt.putString("id", "base_vehicle");
        return result;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        getBukkitEntity().storeBukkitValues(nbt);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        getBukkitEntity().readBukkitValues(nbt);
    }

    public void spawn(Location location) {
        setPos(CraftLocation.toVec3D(location));
        level().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

}
