package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.data.storm.models.FitnessBoosterModel;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.modules.data.utils.StormUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        var configuration = OpenMinetopia.getDefaultConfiguration();
        if (!configuration.isFitnessDeathPunishmentEnabled()) return;

        var player = event.getEntity();
        var minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        int punishmentInMillis = configuration.getFitnessDeathPunishmentDuration() * 60 * 1000;
        StormUtils.getNextId(FitnessBoosterModel.class, FitnessBoosterModel::getId).whenComplete((nextId, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            var fitnessBooster = new FitnessBooster(nextId, configuration.getFitnessDeathPunishmentAmount(), System.currentTimeMillis() + punishmentInMillis);
            minetopiaPlayer.getFitness().addBooster(fitnessBooster);
            minetopiaPlayer.getFitness().getRunnable().run();
        });
    }
}
