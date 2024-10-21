package nl.openminetopia.modules.plots.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
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
            player.sendMessage(MessageConfiguration.component("plot_invalid_location"));
            return;
        }

        if (region.getFlag(PlotModule.PLOT_FLAG) == null) {
            player.sendMessage(MessageConfiguration.component("plot_not_valid"));
            return;
        }

        String owners = region.getOwners().getUniqueIds().stream()
                .map(ownerId -> Bukkit.getOfflinePlayer(ownerId).getName())
                .collect(Collectors.joining(", "));

        String members = region.getMembers().getUniqueIds().stream()
                .map(memberId -> Bukkit.getOfflinePlayer(memberId).getName())
                .collect(Collectors.joining(", "));

        player.sendMessage(MessageConfiguration.component("plot_info_header"));
        // TODO: Replace <plotname> with actual plot name.
        player.sendMessage(MessageConfiguration.component("plot_info_title"));
        player.sendMessage(Component.empty());
        // TODO: Replace <owners> with actual owners. (If there are no owners, replace with "Geen.")
        player.sendMessage(MessageConfiguration.component("plot_info_owners"));
        // TODO: Replace <members> with actual owners. (If there are no owners, replace with "Geen.")
        player.sendMessage(MessageConfiguration.component("plot_info_members"));

        if(region.getFlag(PlotModule.PLOT_DESCRIPTION) != null) {
            String description = region.getFlag(PlotModule.PLOT_DESCRIPTION);
            // TODO: Replace <description> with actual description.
            player.sendMessage(MessageConfiguration.component("plot_info_description"));
        }

        player.sendMessage(MessageConfiguration.component("plot_info_footer"));
    }

}
