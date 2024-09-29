package nl.openminetopia.modules.plots.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.plots.PlotModule;
import nl.openminetopia.utils.ChatUtils;
import nl.openminetopia.utils.WorldGuardUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("plot|p")
public class PlotMembersCommand extends BaseCommand {

    @Subcommand("addmember")
    @Description("Voegt een speler toe aan een plot.")
    @Syntax("<speler>")
    public void addPlotMember(Player player, OnlinePlayer onlinePlayer) {
        ProtectedRegion region = WorldGuardUtils.getProtectedRegion(player.getLocation(), priority -> priority >= 0);
        Player target = onlinePlayer.getPlayer();

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

        if (!region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>Je bent geen eigenaar van dit plot."));
            return;
        }

        if (region.getMembers().contains(target.getUniqueId())) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>" + target.getName() + " is al een member van dit plot."));
            return;
        }

        region.getMembers().addPlayer(target.getUniqueId());
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Je hebt <aqua>" + target.getName() + " <dark_aqua>toegevoegd aan het plot."));
    }

    @Subcommand("removemember")
    @Description("Verwijderd een speler van een plot.")
    @Syntax("<speler>")
    public void removePlotMember(Player player, OfflinePlayer offlinePlayer) {
        ProtectedRegion region = WorldGuardUtils.getProtectedRegion(player.getLocation(), priority -> priority >= 0);
        PlayerProfile profile = offlinePlayer.getPlayerProfile();

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

        if (!region.getOwners().contains(player.getUniqueId())) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>Je bent geen eigenaar van dit plot."));
            return;
        }

        if (!region.getMembers().contains(profile.getId())) {
            player.sendMessage(ChatUtils.format(minetopiaPlayer, "<red>" + profile.getName() + " is geen member van dit plot."));
            return;
        }

        region.getMembers().removePlayer(profile.getId());
        player.sendMessage(ChatUtils.format(minetopiaPlayer, "<dark_aqua>Je hebt <aqua>" + profile.getName() + " <dark_aqua>verwijderd van dit plot."));
    }

}