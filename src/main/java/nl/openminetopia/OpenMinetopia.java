package nl.openminetopia;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.ModuleManager;
import nl.openminetopia.modules.chat.ChatModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.fitness.FitnessModule;
import nl.openminetopia.modules.mod.ModModule;
import nl.openminetopia.modules.player.PlayerModule;
import nl.openminetopia.modules.prefix.PrefixModule;
import nl.openminetopia.modules.scoreboard.ScoreboardModule;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenMinetopia extends JavaPlugin {

    @Getter
    private static OpenMinetopia instance;
    @Getter
    private static ModuleManager moduleManager;
    @Getter
    private static PaperCommandManager commandManager;
    @Getter
    private static DefaultConfiguration defaultConfiguration;
    @Getter
    private static MessageConfiguration messageConfiguration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        commandManager = new PaperCommandManager(this);
        moduleManager = new ModuleManager();

        defaultConfiguration = new DefaultConfiguration(getDataFolder());
        defaultConfiguration.saveConfiguration();

        messageConfiguration = new MessageConfiguration(getDataFolder());
        messageConfiguration.saveConfiguration();

        moduleManager.register(
                new DataModule(),
                new PlayerModule(),
                new FitnessModule(),
                new ModModule(),
                new PrefixModule(),
                new ChatModule(),
                new ScoreboardModule()
        );

        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, ChatColor.DARK_AQUA);
        commandManager.setFormat(MessageType.INFO, ChatColor.DARK_AQUA, ChatColor.AQUA, ChatColor.GRAY);
        commandManager.setFormat(MessageType.ERROR, ChatColor.RED);
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }
}