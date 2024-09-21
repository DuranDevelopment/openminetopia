package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("prefix")
public class PrefixRemoveCommand extends BaseCommand {

    @Subcommand("remove")
    @Syntax("<player> <id>")
    @CommandCompletion("@players")
    @Description("Remove a prefix from a player.")
    public static void addPrefixCommand(Player player, OfflinePlayer offlinePlayer, Integer id) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage("This player does not exist.");
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        for (Prefix prefix : minetopiaPlayer.getPrefixes()) {
            if (prefix.getId() == id) {
                minetopiaPlayer.removePrefix(prefix);
                player.sendMessage(ChatUtils.color("<red>Removed the prefix from the player."));
                return;
            }
        }
        player.sendMessage(ChatUtils.color("<red>This player does not have a prefix with this id."));
    }
}
