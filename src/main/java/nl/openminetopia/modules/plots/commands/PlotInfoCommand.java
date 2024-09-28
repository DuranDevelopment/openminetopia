package nl.openminetopia.modules.plots.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@CommandAlias("plotinfo|pi")
public class PlotInfoCommand extends BaseCommand {

    @Default
    @Description("Bekijk informatie van een plot.")
    public void plotInfo(Player player) {
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

        String owners = region.getOwners().getUniqueIds().stream()
                .map(ownerId -> Bukkit.getOfflinePlayer(ownerId).getName())
                .collect(Collectors.joining(", "));

        String members = region.getMembers().getUniqueIds().stream()
                .map(memberId -> Bukkit.getOfflinePlayer(memberId).getName())
                .collect(Collectors.joining(", "));

        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua><st>----------------------------------------------"));
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Plot informatie voor: <aqua>" + region.getId()));
        player.sendMessage(Component.empty());
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Owners: <aqua>" + (region.getOwners().size() > 0 ? owners : "Geen.")));
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Members: <aqua>" + (region.getMembers().size() > 0 ? members : "Geen.")));

        if(region.getFlag(PlotModule.PLOT_DESCRIPTION) != null) {
            String description = region.getFlag(PlotModule.PLOT_DESCRIPTION);
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Beschrijving: <aqua>" + description));
        }

        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua><st>----------------------------------------------"));
    }

}
