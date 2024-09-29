package nl.openminetopia.modules.fitness.listeners;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.EatingStatistic;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.DefaultConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerEatListener implements Listener {

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {

        if (!event.getItem().getType().isEdible()) return;
        DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

        if (!configuration.getCheapFood().contains(event.getItem().getType().name()) && !configuration.getLuxuryFood().contains(event.getItem().getType().name())) return;

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(event.getPlayer());
        if (minetopiaPlayer == null) return;



        EatingStatistic eatingStatistic = (EatingStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.EATING);

        if (configuration.getCheapFood().contains(event.getItem().getType().name())) {
            eatingStatistic.setPoints(eatingStatistic.getPoints() + configuration.getPointsForCheapFood());
            eatingStatistic.setCheapFood(eatingStatistic.getCheapFood() + 1);
        } else {
            eatingStatistic.setPoints(eatingStatistic.getPoints() + configuration.getPointsForLuxuryFood());
            eatingStatistic.setLuxuryFood(eatingStatistic.getLuxuryFood() + 1);
        }

        double currentEatingPoints = eatingStatistic.getPoints();

        if (eatingStatistic.getPoints() >= 1 && eatingStatistic.getFitnessGained() <= configuration.getMaxFitnessByDrinking()) {
            if (currentEatingPoints % (eatingStatistic.getCheapFood() + eatingStatistic.getLuxuryFood()) == 0) return;
            eatingStatistic.setFitnessGained(eatingStatistic.getFitnessGained() + 1);
            eatingStatistic.setPoints(0);
        }
    }
}
