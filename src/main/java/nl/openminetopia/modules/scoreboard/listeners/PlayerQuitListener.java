package nl.openminetopia.modules.scoreboard.listeners;

import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import nl.openminetopia.api.player.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Sidebar sidebar = ScoreboardManager.getInstance().getScoreboard(player.getUniqueId());
        ScoreboardManager.getInstance().removeScoreboard(player);

        if (sidebar != null) {
            sidebar.close();
        }
    }
}