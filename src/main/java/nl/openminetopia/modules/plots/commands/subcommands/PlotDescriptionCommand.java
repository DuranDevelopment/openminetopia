package nl.openminetopia.modules.plots.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

@CommandAlias("plot|p")
public class PlotDescriptionCommand extends BaseCommand {

    @Subcommand("description")
    @CommandPermission("openminetopia.plot.description")
    @Syntax("<beschrijving>")
    @Description("Set een beschrijving van een plot.")
    public void plotDescription(Player player, String description) {
        ProtectedRegion region = WorldGuardUtils.getProtectedRegion(player.getLocation(), priority -> priority >= 0);

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        if (region == null) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>Je staat niet op een geldig plot."));
            return;
        }

        if (region.getFlag(PlotModule.PLOT_FLAG) == null) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>Dit is geen geldig plot."));
            return;
        }

        if (description.isBlank() || description.equalsIgnoreCase("remove") || description.equalsIgnoreCase("delete") || description.equalsIgnoreCase("null")) {
            region.setFlag(PlotModule.PLOT_DESCRIPTION, null);
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Plot description van <aqua>" + region.getId() + " <dark_aqua>verwijderd."));
            return;
        }

        region.setFlag(PlotModule.PLOT_DESCRIPTION, description);
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Plot description van <aqua>" + region.getId() + " <dark_aqua>veranderd naar <aqua>" + description + "<dark_aqua>."));
    }

}
