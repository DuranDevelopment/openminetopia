package nl.openminetopia.modules.teleporter;

import nl.openminetopia.modules.Module;
import nl.openminetopia.modules.teleporter.commands.TeleporterCommand;
import nl.openminetopia.modules.teleporter.commands.subcommands.TeleporterCreateCommand;
import nl.openminetopia.modules.teleporter.listeners.TeleporterInteractListener;
import nl.openminetopia.modules.teleporter.listeners.block.TeleporterBreakListener;
import nl.openminetopia.modules.teleporter.listeners.block.TeleporterPlaceListener;

public final class TeleporterModule extends Module {

    @Override
    public void enable() {
        registerCommand(new TeleporterCommand());
        registerCommand(new TeleporterCreateCommand());

        registerListener(new TeleporterPlaceListener());
        registerListener(new TeleporterInteractListener());
        registerListener(new TeleporterBreakListener());
    }

    @Override
    public void disable() {

    }

}
