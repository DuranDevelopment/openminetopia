package nl.openminetopia.modules.player.runnables;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.LevelCheckConfiguration;
import nl.openminetopia.modules.player.utils.LevelUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class LevelCheckRunnable extends BukkitRunnable {

    private final OnlineMinetopiaPlayer player;

    public LevelCheckRunnable(OnlineMinetopiaPlayer player) {
        this.player = player;
    }

    @Override
    public void run() {
        LevelCheckConfiguration configuration = OpenMinetopia.getLevelcheckConfiguration();
        if (!player.isInPlace()) return;
        int calculatedLevel = LevelUtil.calculateLevel(player);
        if (configuration.isAutoLevelUp()) {
            player.setLevel(calculatedLevel);
        }
        player.setCalculatedLevel(calculatedLevel);
    }
}
