package nl.openminetopia.modules.plots.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.api.plots.events.PlotCreateEvent;
import nl.openminetopia.configuration.MessageConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("plot|p")
public class PlotDeleteCommand extends BaseCommand {

    @Subcommand("delete")
    @CommandPermission("openminetopia.plot.delete")
    @Syntax("<naam>")
    @CommandCompletion("@plotName")
    @Description("Verwijder een plot.")
    public void deletePlot(Player player, String name) {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(player.getWorld());

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(world);

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        if (regionManager == null) {
            player.sendMessage(MessageConfiguration.component("plot_region_retrieval_error"));
            return;
        }

        ProtectedRegion region = regionManager.getRegion(name);

        if (region == null) {
            player.sendMessage(MessageConfiguration.component("plot_not_found"));
            return;
        }

        PlotCreateEvent event = new PlotCreateEvent(player, region);
        Bukkit.getPluginManager().callEvent(event);

        player.sendMessage(MessageConfiguration.component("plot_deletion_success"));
        regionManager.removeRegion(region.getId());
    }
}
