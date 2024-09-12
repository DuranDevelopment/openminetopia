package nl.openminetopia.modules.fitness;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.fitness.commands.FitnessCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessInfoCommand;
import nl.openminetopia.modules.fitness.listeners.PlayerJoinListener;
import nl.openminetopia.modules.fitness.listeners.PlayerQuitListener;

public class FitnessModule extends Module {

    @Override
    public void enable() {
        registerCommand(new FitnessCommand());
        registerCommand(new FitnessInfoCommand());

        registerListener(new PlayerJoinListener());
        registerListener(new PlayerQuitListener());
    }

    @Override
    public void disable() {

    }
}