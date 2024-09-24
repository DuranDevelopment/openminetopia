package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.PrefixModel;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("prefix")
public class PrefixAddCommand extends BaseCommand {

    @Subcommand("add")
    @Syntax("<player> <expiresAt> <prefix>")
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.prefix.add")
    @Description("Add a prefix to a player.")
    public static void addPrefixCommand(Player player, OfflinePlayer offlinePlayer, Integer expiresAt, String prefix) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage("This player does not exist.");
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;
        player.sendMessage("Added the prefix to the player.");

        Prefix prefix1 = new Prefix(StormDatabase.getInstance().getNextId(PrefixModel.class, PrefixModel::getId), prefix, expiresAt);
        minetopiaPlayer.addPrefix(prefix1);
    }
}