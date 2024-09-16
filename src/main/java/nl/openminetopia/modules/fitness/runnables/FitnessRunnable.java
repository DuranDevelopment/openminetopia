package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.FitnessManager;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FitnessRunnable extends BukkitRunnable {

    private final Player player;

    public FitnessRunnable(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        /* Walking points */

        int currentWalkedDistance = player.getStatistic(Statistic.WALK_ONE_CM); // Huidige afstand in centimeters
        int amountOfCmWalkedPerPoint = configuration.getCmPerWalkingPoint();
        int newWalkingFitness = (currentWalkedDistance - currentWalkedDistance % amountOfCmWalkedPerPoint) / amountOfCmWalkedPerPoint;

        if (minetopiaPlayer.getFitnessGainedByWalking() != newWalkingFitness && newWalkingFitness <= configuration.getMaxFitnessByWalking()) minetopiaPlayer.setFitnessGainedByWalking(newWalkingFitness);

        /* Climbing points */

        int currentClimbingDistance = player.getStatistic(Statistic.CLIMB_ONE_CM); // Huidige afstand in centimeters
        int amountOfCmClimbedPerPoint = configuration.getCmPerClimbingPoint();
        int newClimbingFitness = (currentClimbingDistance - currentClimbingDistance % amountOfCmClimbedPerPoint) / amountOfCmClimbedPerPoint;

        if (minetopiaPlayer.getFitnessGainedByClimbing() != newClimbingFitness && newClimbingFitness <= configuration.getMaxFitnessByClimbing()) minetopiaPlayer.setFitnessGainedByClimbing(newClimbingFitness);

        /* Total points */

        int newTotalFitness = minetopiaPlayer.getFitnessGainedByDrinking() + newWalkingFitness + newClimbingFitness;
        if (minetopiaPlayer.getFitness() != newTotalFitness && newTotalFitness <= configuration.getMaxFitnessLevel()) minetopiaPlayer.setFitness(newTotalFitness);
    }
}
