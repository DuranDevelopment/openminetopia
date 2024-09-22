package nl.openminetopia.modules.admintool;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.admintool.commands.AdminToolCommand;
import nl.openminetopia.modules.admintool.commands.subcommands.AdminToolOpenCommand;

public class AdminToolModule extends Module {
    @Override
    public void enable() {
        registerCommand(new AdminToolCommand());
        registerCommand(new AdminToolOpenCommand());
    }

    @Override
    public void disable() {

    }
}
