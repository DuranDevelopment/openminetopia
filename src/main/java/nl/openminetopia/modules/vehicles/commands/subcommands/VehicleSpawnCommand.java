package nl.openminetopia.modules.vehicles.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.modules.vehicles.objects.Part;
import nl.openminetopia.modules.vehicles.objects.Seat;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

import java.util.Random;

@CommandAlias("vehicle")
public class VehicleSpawnCommand extends BaseCommand {

    @Subcommand("spawn")
    @Description("Spawns a test vehicle.")
    public void spawn(Player player) {
        Vehicle vehicle = new Vehicle(player.getLocation());
        VehiclesModule.vehicles.add(vehicle);

        Seat seat = vehicle.seat(new Vector3f(0, -0.5F, -0.65F), true);
        seat.getEntity().setPassenger(player);

        vehicle.part();
    }

}
