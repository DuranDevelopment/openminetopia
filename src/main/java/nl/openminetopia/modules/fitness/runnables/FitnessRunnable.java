package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.objects.Fitness;
import nl.openminetopia.api.player.fitness.statistics.FitnessStatistic;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.*;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.api.player.fitness.booster.objects.FitnessBooster;
import nl.openminetopia.modules.fitness.utils.FitnessUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FitnessRunnable extends BukkitRunnable {

    private final Fitness fitness;
    private final Player player;
    
    public FitnessRunnable(Fitness fitness) {
        this.fitness = fitness;
        this.player = Bukkit.getPlayer(fitness.getUuid());
    }

    @Override
    public void run() {
        OnlineMinetopiaPlayer minetopiaPlayer = (OnlineMinetopiaPlayer) PlayerManager.getInstance().getMinetopiaPlayer(player);
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

        WalkingStatistic walkingStatistic = (WalkingStatistic) fitness.getStatistic(FitnessStatisticType.WALKING);

        if (walkingStatistic.getFitnessGained() != newWalkingFitness && newWalkingFitness <= walkingStatistic.getMaxFitnessGainable())
            walkingStatistic.setFitnessGained(newWalkingFitness);

        /* Climbing points */

        int currentClimbingDistance = player.getStatistic(Statistic.CLIMB_ONE_CM);
        int amountOfCmClimbedPerPoint = configuration.getCmPerClimbingPoint();
        int newClimbingFitness = FitnessUtils.calculateFitness(currentClimbingDistance, amountOfCmClimbedPerPoint);

        ClimbingStatistic climbingStatistic = (ClimbingStatistic) fitness.getStatistic(FitnessStatisticType.CLIMBING);

        if (climbingStatistic.getFitnessGained() != newClimbingFitness && newClimbingFitness <= climbingStatistic.getMaxFitnessGainable())
            climbingStatistic.setFitnessGained(newClimbingFitness);

        /* Sprinting points */

        int currentSprintingDistance = player.getStatistic(Statistic.SPRINT_ONE_CM);
        int amountOfCmSprintedPerPoint = configuration.getCmPerSprintingPoint();
        int newSprintingFitness = FitnessUtils.calculateFitness(currentSprintingDistance, amountOfCmSprintedPerPoint);

        SprintingStatistic sprintingStatistic = (SprintingStatistic) fitness.getStatistic(FitnessStatisticType.SPRINTING);

        if (sprintingStatistic.getFitnessGained() != newSprintingFitness && newSprintingFitness <= sprintingStatistic.getMaxFitnessGainable())
            sprintingStatistic.setFitnessGained(newSprintingFitness);

        /* Swimming points */

        int currentSwimmingDistance = player.getStatistic(Statistic.SWIM_ONE_CM);
        int amountOfCmSwamPerPoint = configuration.getCmPerSwimmingPoint();
        int newSwimmingFitness = FitnessUtils.calculateFitness(currentSwimmingDistance, amountOfCmSwamPerPoint);

        SwimmingStatistic swimmingStatistic = (SwimmingStatistic) fitness.getStatistic(FitnessStatisticType.SWIMMING);

        if (swimmingStatistic.getFitnessGained() != newSwimmingFitness && newSwimmingFitness <= swimmingStatistic.getMaxFitnessGainable())
            swimmingStatistic.setFitnessGained(newSwimmingFitness);

        /* Flying points */

        int currentFlyingDistance = player.getStatistic(Statistic.AVIATE_ONE_CM);
        int amountOfCmFlownPerPoint = configuration.getCmPerFlyingPoint();
        int newFlyingFitness = FitnessUtils.calculateFitness(currentFlyingDistance, amountOfCmFlownPerPoint);

        FlyingStatistic flyingStatistic = (FlyingStatistic) fitness.getStatistic(FitnessStatisticType.FLYING);

        if (flyingStatistic.getFitnessGained() != newFlyingFitness && newFlyingFitness <= flyingStatistic.getMaxFitnessGainable())
            flyingStatistic.setFitnessGained(newFlyingFitness);


        /* Fitness boosts */

        int fitnessBoost = 0;
        for (int i = 0; i < fitness.getBoosters().size(); i++) {
            FitnessBooster fitnessBooster = fitness.getBoosters().get(i);
            fitnessBoost += fitnessBooster.getAmount();
        }

        /* Eating points */

        EatingStatistic eatingStatistic = (EatingStatistic) fitness.getStatistic(FitnessStatisticType.EATING);

        double eatingPoints = (eatingStatistic.getCheapFood() * configuration.getPointsForCheapFood()) + (eatingStatistic.getLuxuryFood() * configuration.getPointsForLuxuryFood());
        eatingStatistic.setPoints(eatingPoints);

        if (eatingStatistic.getPoints() >= 1 && eatingStatistic.getFitnessGained() <= eatingStatistic.getMaxFitnessGainable()) {
            eatingStatistic.setFitnessGained(eatingStatistic.getFitnessGained() + 1);
            eatingStatistic.setPoints(0);
        }

        /* Total points */

        int newTotalFitness = configuration.getDefaultFitnessLevel();

        for (FitnessStatistic statistic : fitness.getStatistics()) {
            if (statistic.getType() == FitnessStatisticType.TOTAL) continue;
            newTotalFitness += statistic.getFitnessGained();
        }

        newTotalFitness += fitnessBoost;

        TotalStatistic totalStatistic = (TotalStatistic) fitness.getStatistic(FitnessStatisticType.TOTAL);

        if (newTotalFitness > totalStatistic.getMaxFitnessGainable())
            newTotalFitness = totalStatistic.getMaxFitnessGainable();
        if (newTotalFitness < 0) newTotalFitness = 0;

        if (totalStatistic.getFitnessGained() != newTotalFitness) totalStatistic.setFitnessGained(newTotalFitness);

        fitness.apply();
    }
}