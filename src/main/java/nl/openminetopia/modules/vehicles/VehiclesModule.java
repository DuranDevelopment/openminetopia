package nl.openminetopia.modules.vehicles;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.vehicles.commands.VehicleCommand;
import nl.openminetopia.modules.vehicles.commands.subcommands.VehicleSpawnCommand;
import nl.openminetopia.modules.vehicles.entity.BaseVehicleEntity;
import nl.openminetopia.modules.vehicles.listeners.PlayerInputListener;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.utils.nms.EntityUtils;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiclesModule extends Module {

    /* Temporaru */
    public static List<Vehicle> vehicles = new ArrayList<>();

    @Override
    public void enable() {
        PacketEvents.getAPI().getEventManager().registerListener(
                new PlayerInputListener(), PacketListenerPriority.NORMAL);

        registerCommand(new VehicleCommand());
        registerCommand(new VehicleSpawnCommand());
    }

    @Override
    public void disable() {

    }

    public static Optional<Vehicle> vehicleBySeat(ArmorStand entity) {
        return vehicles.stream().filter(vehicle -> vehicle.getSeats().stream()
                        .anyMatch(seat -> seat.getEntity().getUniqueId().equals(entity.getUniqueId())))
                .findAny();
    }

}
