package nl.openminetopia.modules.vehicles.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.modules.vehicles.enums.VehicleEntityType;
import nl.openminetopia.modules.vehicles.objects.Vehicle;
import nl.openminetopia.modules.vehicles.utils.VehicleUtils;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PlayerInputListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        Player player = event.getPlayer();
        if (event.getPacketType() != PacketType.Play.Client.STEER_VEHICLE)
            return;

        WrappedPlayerInputPacket packet = new WrappedPlayerInputPacket(event);
        Entity entity = player.getVehicle();

        Vehicle vehicle = VehiclesModule.vehicleBySeat((ArmorStand) entity);
        Bukkit.getScheduler().runTask(OpenMinetopia.getInstance(), () -> {
            vehicle.tick(packet);
        });
    }

}
