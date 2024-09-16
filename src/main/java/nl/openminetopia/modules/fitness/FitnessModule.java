package nl.openminetopia.modules.fitness;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.fitness.commands.FitnessCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessInfoCommand;
import nl.openminetopia.modules.fitness.listeners.PlayerDrinkListener;

public class FitnessModule extends Module {

    @Override
    public void enable() {
        registerCommand(new FitnessCommand());
        registerCommand(new FitnessInfoCommand());

        registerListener(new PlayerDrinkListener());
    }

    @Override
    public void disable() {

    }
}