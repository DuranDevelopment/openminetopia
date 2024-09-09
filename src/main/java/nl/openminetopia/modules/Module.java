package nl.openminetopia.modules;

import co.aikar.commands.BaseCommand;
import nl.openminetopia.OpenMinetopia;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public abstract class Module {

    public abstract void enable();

    public abstract void disable();

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, OpenMinetopia.getInstance());
    }

    public void registerCommand(BaseCommand command) {
        OpenMinetopia.getCommandManager().registerCommand(command);
    }
}