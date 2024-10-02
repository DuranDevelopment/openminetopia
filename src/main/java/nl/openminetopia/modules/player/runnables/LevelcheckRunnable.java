package nl.openminetopia.modules.player.runnables;

import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.player.utils.LevelUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class LevelcheckRunnable extends BukkitRunnable {

    private final OnlineMinetopiaPlayer player;

    public LevelcheckRunnable(OnlineMinetopiaPlayer player) {
        this.player = player;
    }

    @Override
    public void run() {
        int calculatedLevel = LevelUtil.calculateLevel(player);
        player.setCalculatedLevel(calculatedLevel);
    }
}
