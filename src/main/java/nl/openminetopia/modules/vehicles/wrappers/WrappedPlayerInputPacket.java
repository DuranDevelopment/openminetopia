package nl.openminetopia.modules.vehicles.wrappers;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import lombok.Getter;
import org.bukkit.entity.Entity;

@Getter
public class WrappedPlayerInputPacket {

    private final boolean forward;
    private final boolean backward;
    private final boolean left;
    private final boolean right;
    private final boolean sneak;
    private final boolean jump;

    public WrappedPlayerInputPacket(PacketReceiveEvent event) {
        WrapperPlayClientSteerVehicle wrappedPacket = new WrapperPlayClientSteerVehicle(event);
        forward = wrappedPacket.getForward() > 0;
        backward = wrappedPacket.getForward() < 0;
        left = wrappedPacket.getSideways() > 0;
        right = wrappedPacket.getSideways() < 0;
        sneak = wrappedPacket.isUnmount();
        jump = wrappedPacket.isJump();
    }

}
