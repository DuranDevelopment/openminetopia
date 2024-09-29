package nl.openminetopia.api.player.events;

import lombok.Getter;
import lombok.Setter;
import nl.openminetopia.api.enums.LevelChangeReason;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeLevelEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Setter
    @Getter
    private boolean cancelled = false;

    @Getter
    private final MinetopiaPlayer player;

    private final LevelChangeReason reason;

    @Getter
    private final int newLevel;

    public PlayerChangeLevelEvent(MinetopiaPlayer player, LevelChangeReason reason, int newLevel) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.player = player;
        this.reason = reason;
        this.newLevel = newLevel;
    }

    public LevelChangeReason getLevelChangeReason() {
        return this.reason;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public static boolean setLevel(MinetopiaPlayer target, int newLevel, LevelChangeReason changeReason) {
        PlayerChangeLevelEvent event = new PlayerChangeLevelEvent(target, changeReason, newLevel);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }
}
