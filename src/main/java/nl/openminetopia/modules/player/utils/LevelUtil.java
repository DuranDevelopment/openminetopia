package nl.openminetopia.modules.player.utils;

import lombok.experimental.UtilityClass;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.TotalStatistic;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.LevelCheckConfiguration;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

@UtilityClass
public class LevelUtil {

    public static int calculateLevel(MinetopiaPlayer minetopiaPlayer) {
        LevelCheckConfiguration configuration = OpenMinetopia.getLevelcheckConfiguration();
        double points = 0;

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
        if (player == null) return OpenMinetopia.getDefaultConfiguration().getDefaultLevel();

        for (int plots = WorldGuardUtils.getOwnedRegions(player); plots >= 1; plots--) {
            points += configuration.getPointsPerPlot();
        }
        
        int neededPoints = configuration.getPointsNeededForLevelUp();
        int level = (int) Math.floor(points / neededPoints);

        level = Math.max(OpenMinetopia.getDefaultConfiguration().getDefaultLevel(),
                Math.min(level, OpenMinetopia.getLevelcheckConfiguration().getMaxLevel()));


        return level;
    }
}
