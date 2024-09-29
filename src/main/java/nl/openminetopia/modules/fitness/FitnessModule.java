package nl.openminetopia.modules.fitness;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.fitness.commands.FitnessCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessBoosterCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessInfoCommand;
import nl.openminetopia.modules.fitness.listeners.PlayerDeathListener;
import nl.openminetopia.modules.fitness.listeners.PlayerDrinkListener;

public class FitnessModule extends Module {

    @Override
    public void enable() {
        registerCommand(new FitnessCommand());
        registerCommand(new FitnessInfoCommand());
        registerCommand(new FitnessBoosterCommand());

        registerListener(new PlayerDrinkListener());
        registerListener(new PlayerDeathListener());
    }

    @Override
    public void disable() {

    }
}