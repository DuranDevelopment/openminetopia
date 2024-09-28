package nl.openminetopia.api.plots.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlotCreateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final ProtectedRegion region;

    public PlotCreateEvent(Player player, ProtectedRegion region) {
        this.player = player;
        this.region = region;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
