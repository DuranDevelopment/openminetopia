package nl.openminetopia.modules.scoreboard.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.ScoreboardManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.scoreboard.ScoreboardModule;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ScoreboardModule scoreboardModule = OpenMinetopia.getModuleManager().getModule(ScoreboardModule.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.kick(ChatUtils.color("<red>Er is een fout opgetreden bij het laden van je gegevens! Probeer het later opnieuw."));
            return;
        }

        ((OnlineMinetopiaPlayer) minetopiaPlayer).setScoreboardVisible(true);
        ScoreboardManager.getInstance().addScoreboard(player);

        Bukkit.getServer().getScheduler().runTaskTimer(OpenMinetopia.getInstance(), task -> {
            if (!player.isOnline()) {
                task.cancel();
                return;
            }
            ScoreboardManager.getInstance().updateBoard(minetopiaPlayer);
        }, 0, 10*20);
    }
}