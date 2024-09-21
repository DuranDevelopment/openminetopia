package nl.openminetopia;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.sk89q.worldguard.WorldGuard;
import lombok.Getter;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.ModuleManager;
import nl.openminetopia.modules.chat.ChatModule;
import nl.openminetopia.modules.color.ColorModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.fitness.FitnessModule;
import nl.openminetopia.modules.mod.ModModule;
import nl.openminetopia.modules.places.PlacesModule;
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
                new ColorModule(),
                new PlacesModule(),
                new ScoreboardModule()
        );

        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, 1, ChatColor.GOLD);
        commandManager.setFormat(MessageType.HELP, 2, ChatColor.YELLOW);
        commandManager.setFormat(MessageType.HELP, 3, ChatColor.GRAY);
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }
}