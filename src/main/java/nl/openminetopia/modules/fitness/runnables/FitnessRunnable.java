package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.modules.fitness.utils.FitnessUtils;
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

        int currentWalkedDistance = player.getStatistic(Statistic.WALK_ONE_CM);
        int amountOfCmWalkedPerPoint = configuration.getCmPerWalkingPoint();
        int newWalkingFitness = (currentWalkedDistance - currentWalkedDistance % amountOfCmWalkedPerPoint) / amountOfCmWalkedPerPoint;

        if (minetopiaPlayer.getFitness().getFitnessGainedByWalking() != newWalkingFitness && newWalkingFitness <= configuration.getMaxFitnessByWalking())
            minetopiaPlayer.getFitness().setFitnessGainedByWalking(newWalkingFitness);

        /* Climbing points */

        int currentClimbingDistance = player.getStatistic(Statistic.CLIMB_ONE_CM);
        int amountOfCmClimbedPerPoint = configuration.getCmPerClimbingPoint();
        int newClimbingFitness = (currentClimbingDistance - currentClimbingDistance % amountOfCmClimbedPerPoint) / amountOfCmClimbedPerPoint;

        if (minetopiaPlayer.getFitness().getFitnessGainedByClimbing() != newClimbingFitness && newClimbingFitness <= configuration.getMaxFitnessByClimbing())
            minetopiaPlayer.getFitness().setFitnessGainedByClimbing(newClimbingFitness);

        /* Sprinting points */

        int currentSprintingDistance = player.getStatistic(Statistic.SPRINT_ONE_CM);
        int amountOfCmSprintedPerPoint = configuration.getCmPerSprintingPoint();
        int newSprintingFitness = (currentSprintingDistance - currentSprintingDistance % amountOfCmSprintedPerPoint) / amountOfCmSprintedPerPoint;

        if (minetopiaPlayer.getFitness().getFitnessGainedByClimbing() != newSprintingFitness && newSprintingFitness <= configuration.getMaxFitnessBySprinting())
            minetopiaPlayer.getFitness().setFitnessGainedBySprinting(newSprintingFitness);

        /* Swimming points */

        int currentSwimmingDistance = player.getStatistic(Statistic.SWIM_ONE_CM);
        int amountOfCmSwamPerPoint = configuration.getCmPerSwimmingPoint();
        int newSwimmingFitness = (currentSwimmingDistance - currentSwimmingDistance % amountOfCmSwamPerPoint) / amountOfCmSwamPerPoint;

        if (minetopiaPlayer.getFitness().getFitnessGainedBySwimming() != newSwimmingFitness && newSwimmingFitness <= configuration.getMaxFitnessBySwimming())
            minetopiaPlayer.getFitness().setFitnessGainedBySwimming(newSwimmingFitness);

        /* Flying points */

        int currentFlyingDistance = player.getStatistic(Statistic.AVIATE_ONE_CM);
        int amountOfCmFlownPerPoint = configuration.getCmPerFlyingPoint();
        int newFlyingFitness = (currentFlyingDistance - currentFlyingDistance % amountOfCmFlownPerPoint) / amountOfCmFlownPerPoint;

        if (minetopiaPlayer.getFitness().getFitnessGainedByFlying() != newFlyingFitness && newFlyingFitness <= configuration.getMaxFitnessByFlying())
            minetopiaPlayer.getFitness().setFitnessGainedByFlying(newFlyingFitness);

        /* Fitness boosts */

        int fitnessBoost = 0;
        for (int i = 0; i < minetopiaPlayer.getFitness().getFitnessBoosters().size(); i++) {
            FitnessBooster fitnessBooster = minetopiaPlayer.getFitness().getFitnessBoosters().get(i);
            if (fitnessBooster.getExpiresAt() < System.currentTimeMillis() && fitnessBooster.getExpiresAt() != -1) {
                minetopiaPlayer.getFitness().removeFitnessBooster(fitnessBooster);
                i--;
            } else {
                fitnessBoost += fitnessBooster.getAmount();
            }
        }

        /* Total points */

        int newTotalFitness = configuration.getDefaultFitnessLevel() +
                minetopiaPlayer.getFitness().getFitnessGainedByDrinking() +
                minetopiaPlayer.getFitness().getFitnessGainedByHealth() +
                newWalkingFitness +
                newClimbingFitness +
                newSprintingFitness +
                newSwimmingFitness +
                newFlyingFitness +
                fitnessBoost;

        if (newTotalFitness > configuration.getMaxFitnessLevel()) newTotalFitness = configuration.getMaxFitnessLevel();
        if (newTotalFitness < 0) newTotalFitness = 0;

        if (minetopiaPlayer.getFitness().getTotalFitness() != newTotalFitness) minetopiaPlayer.getFitness().setTotalFitness(newTotalFitness);

        FitnessUtils.applyFitness(player);
    }
}