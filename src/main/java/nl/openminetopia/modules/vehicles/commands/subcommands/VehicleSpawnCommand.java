package nl.openminetopia.modules.vehicles.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.modules.vehicles.configuration.BlueprintManager;
import nl.openminetopia.modules.vehicles.objects.Part;
import nl.openminetopia.modules.vehicles.objects.Seat;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

import java.util.Random;

@CommandAlias("vehicle")
public class VehicleSpawnCommand extends BaseCommand {

    @Subcommand("spawn")
    @Description("Spawns a test vehicle.")
    public void spawn(Player player, String name) {
        BlueprintManager.Blueprint blueprint = BlueprintManager.get(name);
        Vehicle vehicle = blueprint.spawn(player.getLocation());

        for (Seat seat : vehicle.getSeats()) {
            if (!seat.isDriver()) continue;
            seat.getEntity().setPassenger(player);
            break;
        }
    }

}
