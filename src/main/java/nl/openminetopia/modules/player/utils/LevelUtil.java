package nl.openminetopia.modules.player.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.TotalStatistic;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.LevelcheckConfiguration;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

@UtilityClass
public class LevelUtil {

    public static int calculateLevel(MinetopiaPlayer minetopiaPlayer) {
        LevelcheckConfiguration configuration = OpenMinetopia.getLevelcheckConfiguration();
        int points = 0;

        // TODO: Add points per vehicle
        // TODO: Add points per 5k balance

        // Points for having a prefix
        if (minetopiaPlayer.getPrefixes() != null && !minetopiaPlayer.getPrefixes().isEmpty()) {
            points += configuration.getPointsForPrefix();
        }

        // Points per 20 fitness
        TotalStatistic totalStatistic = (TotalStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.TOTAL);
        if (totalStatistic == null) return 0;
        for (int fitness = totalStatistic.getFitnessGained(); fitness >= 20; fitness -= 20) {
            points += configuration.getPointsPer20Fitness();
        }

        // Points per 1 hour playtime
        for (int playtime = minetopiaPlayer.getPlaytime(); playtime >= 3600; playtime -= 3600) {
            points += configuration.getPointsPerHourPlayed();
        }

        // Points per plot
        Player player = minetopiaPlayer.getBukkit().getPlayer();
        if (player == null) return points;

        for (int plots = WorldGuardUtils.getOwnedRegions(player); plots >= 1; plots--) {
            points += configuration.getPointsPerPlot();
        }

        // Check points needed for a level up (for example 2500 points per level)
        int level = 0;
        int neededPoints = configuration.getPointsNeededForLevelUp();
        while (points >= neededPoints) {
            points -= neededPoints;
            level++;
            neededPoints += configuration.getPointsNeededForLevelUp();
        }

        // Check if level isn't greater than the max level
        if (level > OpenMinetopia.getLevelcheckConfiguration().getMaxLevel()) {
            level = OpenMinetopia.getLevelcheckConfiguration().getMaxLevel();
        }

        // Check if level isn't lower than the default level
        if (level < OpenMinetopia.getDefaultConfiguration().getDefaultLevel()) {
            level = OpenMinetopia.getDefaultConfiguration().getDefaultLevel();
        }

        return level;
    }
}
