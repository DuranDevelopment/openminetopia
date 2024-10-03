package nl.openminetopia.modules.prefix;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.prefix.commands.PrefixCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixAddCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixMenuCommand;
import nl.openminetopia.modules.prefix.commands.subcommands.PrefixRemoveCommand;

public class PrefixModule extends Module {
    @Override
    public void enable() {
        registerCommand(new PrefixCommand());
        registerCommand(new PrefixMenuCommand());
        registerCommand(new PrefixAddCommand());
        registerCommand(new PrefixRemoveCommand());
    }

    @Override
    public void disable() {

    }
}
