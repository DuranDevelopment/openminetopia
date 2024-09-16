package nl.openminetopia.modules.fitness;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.fitness.commands.FitnessCommand;
import nl.openminetopia.modules.fitness.commands.subcommands.FitnessInfoCommand;

public class FitnessModule extends Module {

    @Override
    public void enable() {
        registerCommand(new FitnessCommand());
        registerCommand(new FitnessInfoCommand());
    }

    @Override
    public void disable() {

    }
}