package nl.openminetopia.modules.mod;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.mod.commands.ModCommand;
import nl.openminetopia.modules.mod.commands.subcommands.ModSetLevelCommand;

public class ModModule extends Module {
    @Override
    public void enable() {
        registerCommand(new ModCommand());
        registerCommand(new ModSetLevelCommand());
    }

    @Override
    public void disable() {

    }
}
