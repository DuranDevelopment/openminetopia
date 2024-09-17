package nl.openminetopia.modules.player.runnables;

import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaytimeRunnable extends BukkitRunnable {

    private final Player player;

    public PlaytimeRunnable(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        int newPlaytime = minetopiaPlayer.getPlaytime() + 1;

        // If the new playtime is a multiple of 60, update the playtime in the database
        if (newPlaytime % 60 == 0) {
            minetopiaPlayer.setPlaytime(newPlaytime, true);
            return;
        }
        minetopiaPlayer.setPlaytime(newPlaytime, false);
    }
}
