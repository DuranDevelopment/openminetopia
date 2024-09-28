package nl.openminetopia.modules.player;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.player.commands.PlaytimeCommand;
import nl.openminetopia.modules.player.listeners.PlayerJoinListener;
import nl.openminetopia.modules.player.listeners.PlayerPreLoginListener;
import nl.openminetopia.modules.player.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerModule extends Module {

    @Override
    public void enable() {
        registerListener(new PlayerPreLoginListener());
        registerListener(new PlayerJoinListener());
        registerListener(new PlayerQuitListener());

        registerCommand(new PlaytimeCommand());

        Bukkit.getScheduler().runTaskTimerAsynchronously(OpenMinetopia.getInstance(), () -> {
            for (MinetopiaPlayer minetopiaPlayer : PlayerManager.getInstance().getMinetopiaPlayers().values()) {
                if (!(minetopiaPlayer instanceof OnlineMinetopiaPlayer onlineMinetopiaPlayer)) continue;
                onlineMinetopiaPlayer.save().whenComplete((unused, throwable) -> {
                    if (throwable != null) throwable.printStackTrace();
                });
            }
        }, 0, 20 * 60 * 5); // Save every 5 minutes
    }

    @Override
    public void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            OnlineMinetopiaPlayer minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
            if (minetopiaPlayer == null) continue;
            minetopiaPlayer.save().whenComplete((unused, throwable) -> {
                if (throwable != null) throwable.printStackTrace();
            });
        }
    }
}
