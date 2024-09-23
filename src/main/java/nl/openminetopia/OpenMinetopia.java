package nl.openminetopia;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.jazzkuh.inventorylib.objects.Menu;
import com.jeff_media.customblockdata.CustomBlockData;
import lombok.Getter;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.ModuleManager;
import nl.openminetopia.modules.admintool.AdminToolModule;
import nl.openminetopia.modules.chat.ChatModule;
import nl.openminetopia.modules.color.ColorModule;
import nl.openminetopia.modules.config.ConfigModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.fitness.FitnessModule;
import nl.openminetopia.modules.mod.ModModule;
import nl.openminetopia.modules.places.PlacesModule;
import nl.openminetopia.modules.player.PlayerModule;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.modules.prefix.PrefixModule;
import nl.openminetopia.modules.scoreboard.ScoreboardModule;
import nl.openminetopia.modules.teleporter.TeleporterModule;
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

        CustomBlockData.registerListener(this);

        try {
            defaultConfiguration = new DefaultConfiguration(getDataFolder());
            defaultConfiguration.saveConfiguration();
        } catch (Exception e) {
            this.getLogger().severe("An error occurred while loading the configuration file.");
            e.printStackTrace();
        }
        defaultConfiguration = new DefaultConfiguration(getDataFolder());
        defaultConfiguration.saveConfiguration();

        messageConfiguration = new MessageConfiguration(getDataFolder());
        messageConfiguration.saveConfiguration();

        moduleManager.register(
                new ConfigModule(),
                new DataModule(),
                new PlayerModule(),
                new FitnessModule(),
                new ModModule(),
                new PrefixModule(),
                new ChatModule(),
                new ColorModule(),
                new PlacesModule(),
                new ScoreboardModule(),
                new PlotModule(),
                new AdminToolModule(),
                new TeleporterModule()
        );

        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, 1, ChatColor.GOLD);
        commandManager.setFormat(MessageType.HELP, 2, ChatColor.YELLOW);
        commandManager.setFormat(MessageType.HELP, 3, ChatColor.GRAY);

        Menu.init(this);
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }
}