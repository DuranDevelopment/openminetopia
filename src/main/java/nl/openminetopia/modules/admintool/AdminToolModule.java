package nl.openminetopia.modules.admintool;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.admintool.commands.AdminToolCommand;
import nl.openminetopia.modules.admintool.commands.subcommands.AdminToolGetCommand;
import nl.openminetopia.modules.admintool.commands.subcommands.AdminToolOpenCommand;
import nl.openminetopia.modules.admintool.listeners.PlayerEntityInteractListener;
import nl.openminetopia.modules.admintool.listeners.PlayerInteractListener;

public class AdminToolModule extends Module {
    @Override
    public void enable() {
        registerCommand(new AdminToolCommand());
        registerCommand(new AdminToolOpenCommand());
        registerCommand(new AdminToolGetCommand());

        registerListener(new PlayerInteractListener());
        registerListener(new PlayerEntityInteractListener());
    }

    @Override
    public void disable() {

    }
}
