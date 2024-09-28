package nl.openminetopia.modules.fitness;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.fitness.statistics.enums.FitnessStatisticType;
import nl.openminetopia.api.player.fitness.statistics.types.HealthStatistic;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.fitness.commands.FitnessCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessBoosterCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessInfoCommand;
import nl.openminetopia.modules.fitness.listeners.PlayerDeathListener;
import nl.openminetopia.modules.fitness.listeners.PlayerDrinkListener;
import org.bukkit.Bukkit;

public class FitnessModule extends Module {

    @Override
    public void enable() {
        registerCommand(new FitnessCommand());
        registerCommand(new FitnessInfoCommand());
        registerCommand(new FitnessBoosterCommand());

        registerListener(new PlayerDrinkListener());
        registerListener(new PlayerDeathListener());

        Bukkit.getScheduler().runTaskTimerAsynchronously(OpenMinetopia.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> {
            var minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
            if (minetopiaPlayer == null) return;

            HealthStatistic healthStatistic = (HealthStatistic) minetopiaPlayer.getFitness().getStatistic(FitnessStatisticType.HEALTH);

            int newHealthPoints;

            if (player.getFoodLevel() >= 18) {
                newHealthPoints = healthStatistic.getPoints() + OpenMinetopia.getDefaultConfiguration().getPointsAbove9Hearts();
                healthStatistic.setPoints(newHealthPoints);
            } else if (player.getFoodLevel() <= 4) {
                newHealthPoints = healthStatistic.getPoints() + OpenMinetopia.getDefaultConfiguration().getPointsBelow2Hearts();
                healthStatistic.setPoints(newHealthPoints);
            } else if (player.getFoodLevel() <= 10) {
                newHealthPoints = healthStatistic.getPoints() + OpenMinetopia.getDefaultConfiguration().getPointsBelow5Hearts();
                healthStatistic.setPoints(newHealthPoints);
            }

            if (healthStatistic.getPoints() >= 1 && healthStatistic.getPoints() <= healthStatistic.getMaxFitnessGainable()) {
                healthStatistic.setFitnessGained(healthStatistic.getFitnessGained() + 1);
                healthStatistic.setPoints(0);
            }
        }), 0, 4 * 60 * 20); // Run timer every 4 minutes
    }

    @Override
    public void disable() {

    }
}