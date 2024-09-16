package nl.openminetopia.modules.color;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.color.commands.ColorCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorAddCommand;
import nl.openminetopia.modules.color.commands.subcommands.ColorSelectCommand;

public class ColorModule extends Module {

    @Override
    public void enable() {
        registerCommand(new ColorCommand());
        registerCommand(new ColorSelectCommand());
        registerCommand(new ColorAddCommand());
    }

    @Override
    public void disable() {

    }
}
