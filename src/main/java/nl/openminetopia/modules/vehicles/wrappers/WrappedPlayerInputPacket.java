package nl.openminetopia.modules.vehicles.wrappers;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WrappedPlayerInputPacket {

    private final boolean forward, backward, left, right, sneak, jump;

    public WrappedPlayerInputPacket(PacketReceiveEvent event) {
        WrapperPlayClientSteerVehicle wrappedPacket = new WrapperPlayClientSteerVehicle(event);
        forward = wrappedPacket.getForward() > 0;
        backward = wrappedPacket.getForward() < 0;
        left = wrappedPacket.getSideways() > 0;
        right = wrappedPacket.getSideways() < 0;
        sneak = wrappedPacket.isUnmount();
        jump = wrappedPacket.isJump();
    }

    public WrappedPlayerInputPacket() {
        forward = false;
        backward = false;
        left = false;
        right = false;
        sneak = false;
        jump = false;
    }

}
