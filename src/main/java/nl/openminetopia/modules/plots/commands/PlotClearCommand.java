package nl.openminetopia.modules.plots.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.entity.Player;

@CommandAlias("plot|p")
public class PlotClearCommand extends BaseCommand {

    @Subcommand("clear")
    @CommandPermission("openminetopia.plot.clear")
    @Description("Verwijderd alle members en spelers van een plot.")
    public void plotClear(Player player) {
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

        region.getOwners().clear();
        region.getMembers().clear();

        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Je hebt alle plot <aqua>owners <dark_aqua>en <aqua>members <dark_aqua>verwijderd."));
    }

}
