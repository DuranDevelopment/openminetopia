package nl.openminetopia.modules.vehicles;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.vehicles.listeners.PlayerInputListener;

public class VehiclesModule extends Module {
    @Override
    public void enable() {
        PacketEvents.getAPI().getEventManager().registerListener(
                new PlayerInputListener(), PacketListenerPriority.NORMAL);
    }

    @Override
    public void disable() {

    }
}
