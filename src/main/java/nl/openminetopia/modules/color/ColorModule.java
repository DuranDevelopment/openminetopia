package nl.openminetopia.modules.color;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.color.commands.ColorCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorAddCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorCreateCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorRemoveCommand;

public class ColorModule extends Module {

    @Override
    public void enable() {
        registerCommand(new ColorCommand());
        registerCommand(new ColorAddCommand());
        registerCommand(new ColorRemoveCommand());
        registerCommand(new ColorCreateCommand());
    }

    @Override
    public void disable() {

    }
}
