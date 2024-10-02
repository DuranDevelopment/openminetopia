package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.api.player.fitness.objects.Fitness;
import org.bukkit.scheduler.BukkitRunnable;

public class FitnessBoosterRunnable extends BukkitRunnable {

    private final Fitness fitness;

    public FitnessBoosterRunnable(Fitness fitness) {
        this.fitness = fitness;
    }

    @Override
    public void run() {
        fitness.getBoosters().forEach(booster -> {
            boolean triggerUpdate = false;
            if (booster.isExpired()) {
                fitness.removeBooster(booster);
                triggerUpdate = true;
            }
            if (triggerUpdate) fitness.getRunnable().run();
        });
    }
}
