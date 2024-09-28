package nl.openminetopia.modules.vehicles.objects;

import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

@Getter
public class Part {

    private final Vehicle vehicle;
    private final ItemDisplay entity;
    private final Entity internalEntity;

    @Getter(AccessLevel.PRIVATE)
    private float oldRadians = 0.0F;

    public Part(Vehicle vehicle) {
        this.vehicle = vehicle;

        this.entity = spawn();
        this.internalEntity = ((CraftEntity)entity).getHandle();
    }

    public Part(Vehicle vehicle, ItemDisplay entity) {
        this.vehicle = vehicle;

        this.entity = entity;
        this.internalEntity = ((CraftEntity)entity).getHandle();
    }

    public void tick() {
        if (!vehicle.getEntity().getPassengers().contains(entity)) {
            vehicle.getEntity().addPassenger(entity);
        }

        transform();
    }

    private ItemDisplay spawn() {
        Location location = vehicle.location();
        location.setPitch(0); location.setYaw(0);

        ItemDisplay entity = vehicle.location().getWorld().spawn(location, ItemDisplay.class);
        entity.setItemStack(new ItemStack(Material.IRON_HOE));
        entity.setViewRange(0.595F);
        entity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.HEAD);

        Transformation transformation = entity.getTransformation();
        transformation.getScale().set(0.62); /* Could be more precise */
        transformation.getTranslation().set(0, -0.3, 0); /* Could be more precise */
        entity.setTransformation(transformation);

        return entity;
    }

    private void transform() {
        if (oldRadians != vehicle.radians()) {
            entity.setInterpolationDelay(0);
            entity.setInterpolationDuration(4);

            Transformation transformation = entity.getTransformation();
            transformation.getRightRotation().setAngleAxis(vehicle.radians(), 0, -1, 0);
            entity.setTransformation(transformation);

            oldRadians = vehicle.radians();
        }
    }

    public String serializableUuid() {
        return entity.getUniqueId().toString();
    }

}
