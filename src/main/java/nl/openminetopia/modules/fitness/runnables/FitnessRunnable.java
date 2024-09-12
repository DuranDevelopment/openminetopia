package nl.openminetopia.modules.fitness.runnables;

import nl.openminetopia.OpenMinetopia;
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
        int amountOfCmWalkedPerPoint = configuration.getWalkingPointsPerCm();
        int newWalkingPoints = (currentWalkedDistance - currentWalkedDistance % amountOfCmWalkedPerPoint) / amountOfCmWalkedPerPoint;  // Eén punt per 1000 cm

        minetopiaPlayer.setWalkingPoints(newWalkingPoints);

        /* Climbing points */

        int currentClimbingDistance = player.getStatistic(Statistic.CLIMB_ONE_CM); // Huidige afstand in centimeters
        int amountOfCmClimbedPerPoint = configuration.getClimbingPointsPerCm();
        int newClimbingPoints = (currentClimbingDistance - currentClimbingDistance % amountOfCmClimbedPerPoint) / amountOfCmClimbedPerPoint;  // Eén punt per 1000 cm

        minetopiaPlayer.setClimbingPoints(newClimbingPoints);

        /* Total points */

        int newTotalPoints = newWalkingPoints + newClimbingPoints;
        minetopiaPlayer.setTotalPoints(newTotalPoints);

    }
}
