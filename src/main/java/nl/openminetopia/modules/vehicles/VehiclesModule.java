package nl.openminetopia.modules.vehicles;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.vehicles.commands.VehicleCommand;
import nl.openminetopia.modules.vehicles.commands.subcommands.VehicleSpawnCommand;
import nl.openminetopia.modules.vehicles.listeners.PlayerInputListener;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.List;

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

    public static Vehicle vehicleBySeat(ArmorStand entity) {
        return vehicles.stream().filter(vehicle -> vehicle.getSeats().stream()
                .anyMatch(seat -> seat.getEntity().getUniqueId().equals(entity.getUniqueId())))
                .findAny().orElse(null);
    }

}
