package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
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

        if (!minetopiaPlayer.isInPlace()) {
            FitnessUtils.clearFitnessEffects(player);
            return;
        }

        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        /* Walking points */

        int currentWalkedDistance = player.getStatistic(Statistic.WALK_ONE_CM);
        int amountOfCmWalkedPerPoint = configuration.getCmPerWalkingPoint();
        int newWalkingFitness = FitnessUtils.calculateFitness(currentWalkedDistance, amountOfCmWalkedPerPoint);

        WalkingStatistic walkingStatistic = (WalkingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.WALKING);

        if (walkingStatistic.getFitnessGained() != newWalkingFitness && newWalkingFitness <= walkingStatistic.getMaxFitnessGainable())
            walkingStatistic.setFitnessGained(newWalkingFitness);

        /* Climbing points */

        int currentClimbingDistance = player.getStatistic(Statistic.CLIMB_ONE_CM);
        int amountOfCmClimbedPerPoint = configuration.getCmPerClimbingPoint();
        int newClimbingFitness = FitnessUtils.calculateFitness(currentClimbingDistance, amountOfCmClimbedPerPoint);

        ClimbingStatistic climbingStatistic = (ClimbingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.CLIMBING);

        if (climbingStatistic.getFitnessGained() != newClimbingFitness && newClimbingFitness <= configuration.getMaxFitnessByClimbing())
            climbingStatistic.setFitnessGained(newClimbingFitness);

        /* Sprinting points */

        int currentSprintingDistance = player.getStatistic(Statistic.SPRINT_ONE_CM);
        int amountOfCmSprintedPerPoint = configuration.getCmPerSprintingPoint();
        int newSprintingFitness = FitnessUtils.calculateFitness(currentSprintingDistance, amountOfCmSprintedPerPoint);

        SprintingStatistic sprintingStatistic = (SprintingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SPRINTING);

        if (sprintingStatistic.getFitnessGained() != newSprintingFitness && newSprintingFitness <= configuration.getMaxFitnessBySprinting())
            sprintingStatistic.setFitnessGained(newSprintingFitness);

        /* Swimming points */

        int currentSwimmingDistance = player.getStatistic(Statistic.SWIM_ONE_CM);
        int amountOfCmSwamPerPoint = configuration.getCmPerSwimmingPoint();
        int newSwimmingFitness = FitnessUtils.calculateFitness(currentSwimmingDistance, amountOfCmSwamPerPoint);

        SwimmingStatistic swimmingStatistic = (SwimmingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.SWIMMING);

        if (swimmingStatistic.getFitnessGained() != newSwimmingFitness && newSwimmingFitness <= configuration.getMaxFitnessBySwimming())
            swimmingStatistic.setFitnessGained(newSwimmingFitness);

        /* Flying points */

        int currentFlyingDistance = player.getStatistic(Statistic.AVIATE_ONE_CM);
        int amountOfCmFlownPerPoint = configuration.getCmPerFlyingPoint();
        int newFlyingFitness = FitnessUtils.calculateFitness(currentFlyingDistance, amountOfCmFlownPerPoint);

        FlyingStatistic flyingStatistic = (FlyingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.FLYING);

        if (flyingStatistic.getFitnessGained() != newFlyingFitness && newFlyingFitness <= configuration.getMaxFitnessByFlying())
            flyingStatistic.setFitnessGained(newFlyingFitness);

        /* Fitness boosts */

        int fitnessBoost = 0;
        for (int i = 0; i < minetopiaPlayer.getFitness().getBoosters().size(); i++) {
            FitnessBooster fitnessBooster = minetopiaPlayer.getFitness().getBoosters().get(i);
            if (fitnessBooster.getExpiresAt() < System.currentTimeMillis() && fitnessBooster.getExpiresAt() != -1) {
                minetopiaPlayer.getFitness().removeBooster(fitnessBooster);
                i--;
            } else {
                fitnessBoost += fitnessBooster.getAmount();
            }
        }

        /* Total points */

        int newTotalFitness = configuration.getDefaultFitnessLevel();

        for (FitnessStatistic statistic : minetopiaPlayer.getFitness().getStatistics()) {
            if (statistic.getType() == FitnessStatisticType.TOTAL) continue;
            newTotalFitness += statistic.getFitnessGained();
        }

        newTotalFitness += fitnessBoost;

        if (newTotalFitness > configuration.getMaxFitnessLevel()) newTotalFitness = configuration.getMaxFitnessLevel();
        if (newTotalFitness < 0) newTotalFitness = 0;

        TotalStatistic totalStatistic = (TotalStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL);
        if (totalStatistic.getFitnessGained() != newTotalFitness) totalStatistic.setFitnessGained(newTotalFitness);

        FitnessUtils.applyFitness(player);
    }
}