package nl.openminetopia.modules.scoreboard.listeners;

import fr.mrmicky.fastboard.adventure.FastBoard;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.ScoreboardManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.kick(ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            return;
        }

        FastBoard board = new FastBoard(player);

        Bukkit.getServer().getScheduler().runTaskTimer(OpenMinetopia.getInstance(), () -> {
            for (FastBoard board2 : ScoreboardManager.getInstance().getScoreboards().values()) {
                ScoreboardManager.getInstance().updateBoard(minetopiaPlayer, board2);
            }
        }, 0, 20);

        ScoreboardManager.getInstance().getScoreboards().put(player.getUniqueId(), board);
    }
}
