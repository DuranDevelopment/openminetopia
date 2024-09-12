package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.PrefixManager;
import nl.openminetopia.api.player.objects.OnlineMinetopiaPlayer;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("prefix")
public class PrefixAddCommand extends BaseCommand {

    @Subcommand("add")
    @Syntax("<player> <prefix>")
    @CommandCompletion("@players")
    @Description("Add a prefix to a player.")
    public static void addPrefixCommand(Player player, OfflinePlayer offlinePlayer, String prefix) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage("This player does not exist.");
            return;
        }

        OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;
        player.sendMessage("Added the prefix to the player.");

        Prefix prefix1 = new Prefix(PrefixManager.getInstance().getNextId(), prefix, -1);
        minetopiaPlayer.addPrefix(prefix1);
    }
}