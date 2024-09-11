package nl.openminetopia;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.modules.ModuleManager;
import nl.openminetopia.modules.chat.ChatModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.mod.ModModule;
import nl.openminetopia.modules.player.PlayerModule;
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

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        commandManager = new PaperCommandManager(this);
        moduleManager = new ModuleManager();

        defaultConfiguration = new DefaultConfiguration(getDataFolder());
        defaultConfiguration.saveConfiguration();

        moduleManager.register(
                new DataModule(),
                new PlayerModule(),
                new ModModule(),
                new ChatModule()
        );

        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, ChatColor.DARK_AQUA);
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }
}
