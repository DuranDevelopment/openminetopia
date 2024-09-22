package nl.openminetopia.modules.vehicles.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import nl.openminetopia.modules.vehicles.wrappers.WrappedPlayerInputPacket;

public class PlayerInputListener implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        User user = event.getUser();
        if (event.getPacketType() != PacketType.Play.Client.STEER_VEHICLE)
            return;

        WrappedPlayerInputPacket packet = new WrappedPlayerInputPacket(event);
        // implement here.
    }

}
