package nl.openminetopia.modules.scoreboard.listeners;

import fr.mrmicky.fastboard.adventure.FastBoard;
import nl.openminetopia.api.player.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        FastBoard board = ScoreboardManager.getInstance().getScoreboard(player.getUniqueId());
        
        if (board != null) {
            board.delete();
        }
    }
}