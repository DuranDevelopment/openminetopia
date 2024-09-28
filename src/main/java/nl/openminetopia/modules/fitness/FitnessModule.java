package nl.openminetopia.modules.fitness;

import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
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

            int newHealthPoints;
            if (player.getFoodLevel() >= 18) {
                newHealthPoints = minetopiaPlayer.getFitness().getHealthPoints() + OpenMinetopia.getDefaultConfiguration().getPointsAbove9Hearts();
                minetopiaPlayer.getFitness().setHealthPoints(newHealthPoints);
            } else if (player.getFoodLevel() <= 4) {
                newHealthPoints = minetopiaPlayer.getFitness().getHealthPoints() + OpenMinetopia.getDefaultConfiguration().getPointsBelow2Hearts();
                minetopiaPlayer.getFitness().setHealthPoints(newHealthPoints);
            } else if (player.getFoodLevel() <= 10) {
                newHealthPoints = minetopiaPlayer.getFitness().getHealthPoints() + OpenMinetopia.getDefaultConfiguration().getPointsBelow5Hearts();
                minetopiaPlayer.getFitness().setHealthPoints(newHealthPoints);
            }

            DefaultConfiguration configuration = OpenMinetopia.getDefaultConfiguration();

            if (minetopiaPlayer.getFitness().getHealthPoints() >= 1 && minetopiaPlayer.getFitness().getHealthPoints() <= configuration.getMaxFitnessByHealth()) {
                minetopiaPlayer.getFitness().setFitnessGainedByHealth(minetopiaPlayer.getFitness().getFitnessGainedByHealth() + 1);
                minetopiaPlayer.getFitness().setHealthPoints(0);
            }
        }), 0, 4 * 60 * 20); // Run timer every 4 minutes
    }

    @Override
    public void disable() {

    }
}