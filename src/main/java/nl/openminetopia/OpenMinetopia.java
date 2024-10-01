package nl.openminetopia;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.jazzkuh.inventorylib.loader.InventoryLoader;
import com.jazzkuh.inventorylib.objects.Menu;
import com.jeff_media.customblockdata.CustomBlockData;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import nl.openminetopia.configuration.DefaultConfiguration;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.ModuleManager;
import nl.openminetopia.modules.chat.ChatModule;
import nl.openminetopia.modules.color.ColorModule;
import nl.openminetopia.modules.core.CoreModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.fitness.FitnessModule;
import nl.openminetopia.modules.staff.StaffModule;
import nl.openminetopia.modules.places.PlacesModule;
import nl.openminetopia.modules.player.PlayerModule;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.modules.prefix.PrefixModule;
import nl.openminetopia.modules.scoreboard.ScoreboardModule;
import nl.openminetopia.modules.teleporter.TeleporterModule;
import nl.openminetopia.modules.vehicles.VehiclesModule;
import nl.openminetopia.modules.vehicles.entity.BaseVehicleEntity;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.placeholderapi.OpenMinetopiaExpansion;
import nl.openminetopia.utils.nms.EntityUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenMinetopia extends JavaPlugin {

    @Getter
    private static OpenMinetopia instance;
    @Getter
    private static ModuleManager moduleManager;
    @Getter
    private static PaperCommandManager commandManager;
    @Getter @Setter
    private static DefaultConfiguration defaultConfiguration;
    @Getter
    private static MessageConfiguration messageConfiguration;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        if (!EntityUtils.isInjected("base_vehicle")) {
            try {
                EntityUtils.injectEntity("base_vehicle", EntityType.ARMOR_STAND, BaseVehicleEntity::new, MobCategory.MISC);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        commandManager = new PaperCommandManager(this);
        moduleManager = new ModuleManager();

        CustomBlockData.registerListener(this);

        defaultConfiguration = new DefaultConfiguration(getDataFolder());
        defaultConfiguration.saveConfiguration();

        messageConfiguration = new MessageConfiguration(getDataFolder());
        messageConfiguration.saveConfiguration();

        moduleManager.register(
                new CoreModule(),
                new DataModule(),
                new PlayerModule(),
                new FitnessModule(),
                new StaffModule(),
                new PrefixModule(),
                new ChatModule(),
                new ColorModule(),
                new PlacesModule(),
                new ScoreboardModule(),
                new PlotModule(),
                new TeleporterModule(),
                new VehiclesModule()
        );

        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, 1, ChatColor.GOLD);
        commandManager.setFormat(MessageType.HELP, 2, ChatColor.YELLOW);
        commandManager.setFormat(MessageType.HELP, 3, ChatColor.GRAY);

        Menu.init(this);
        InventoryLoader.setFormattingProvider(message -> ChatUtils.color("<red>" + message));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) new OpenMinetopiaExpansion().register();
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
        PacketEvents.getAPI().terminate();
    }
}