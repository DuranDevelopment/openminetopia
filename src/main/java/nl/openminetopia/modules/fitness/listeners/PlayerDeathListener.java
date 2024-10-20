package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.objects.FitnessBooster;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void playerDeath(final PlayerDeathEvent event) {
        var configuration = OpenMinetopia.getFitnessConfiguration();
        if (!configuration.isFitnessDeathPunishmentEnabled()) return;

        var player = event.getEntity();
        var minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        int punishmentInMillis = configuration.getFitnessDeathPunishmentDuration() * 60 * 1000;

        var fitnessBooster = new FitnessBooster(configuration.getFitnessDeathPunishmentAmount(), System.currentTimeMillis() + punishmentInMillis);
        minetopiaPlayer.getFitness().addBooster(fitnessBooster);
        minetopiaPlayer.getFitness().getRunnable().run();
    }
}
