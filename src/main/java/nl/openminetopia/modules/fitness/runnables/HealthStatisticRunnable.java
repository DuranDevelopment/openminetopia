package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.fitness.utils.FitnessUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class HealthStatisticRunnable extends BukkitRunnable {

    private final MinetopiaPlayer minetopiaPlayer;

    public HealthStatisticRunnable(MinetopiaPlayer minetopiaPlayer) {
        this.minetopiaPlayer = minetopiaPlayer;
    }

    @Override
    public void run() {
        // check if playtime is a multiple of 3600
        if (minetopiaPlayer.getPlaytime() % 3600 == 0) FitnessUtils.healthCheck(minetopiaPlayer);
    }
}
