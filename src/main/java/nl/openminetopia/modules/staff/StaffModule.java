package nl.openminetopia.modules.staff;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.staff.admintool.commands.AdminToolCommand;
import nl.openminetopia.modules.staff.admintool.commands.subcommands.AdminToolGetCommand;
import nl.openminetopia.modules.staff.admintool.commands.subcommands.AdminToolOpenCommand;
import nl.openminetopia.modules.staff.admintool.listeners.PlayerEntityInteractListener;
import nl.openminetopia.modules.staff.admintool.listeners.PlayerInteractListener;
import nl.openminetopia.modules.staff.chat.commands.StaffchatCommand;
import nl.openminetopia.modules.staff.chat.listeners.PlayerChatListener;
import nl.openminetopia.modules.staff.mod.commands.ModCommand;
import nl.openminetopia.modules.staff.mod.commands.subcommands.ModChatSpyCommand;
import nl.openminetopia.modules.staff.mod.commands.subcommands.ModCommandSpyCommand;
import nl.openminetopia.modules.staff.mod.commands.subcommands.ModSetLevelCommand;

public class StaffModule extends Module {
    @Override
    public void enable() {
        registerCommand(new ModCommand());
        registerCommand(new ModSetLevelCommand());
        registerCommand(new ModChatSpyCommand());
        registerCommand(new ModCommandSpyCommand());

        registerCommand(new AdminToolCommand());
        registerCommand(new AdminToolOpenCommand());
        registerCommand(new AdminToolGetCommand());

        registerCommand(new StaffchatCommand());
        registerListener(new PlayerChatListener());

        registerListener(new PlayerInteractListener());
        registerListener(new PlayerEntityInteractListener());
    }

    @Override
    public void disable() {

    }
}
