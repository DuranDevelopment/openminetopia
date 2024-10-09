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
    @Syntax("<player> <prefix>")
    @CommandPermission("openminetopia.prefix.remove")
    @CommandCompletion("@players @playerPrefixes")
    @Description("Remove a prefix from a player.")
    public void removePrefix(Player player, OfflinePlayer offlinePlayer, String prefixName) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage("This player does not exist.");
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        for (Prefix prefix : minetopiaPlayer.getPrefixes()) {
            if (prefix.getPrefix().equalsIgnoreCase(prefixName)) {
                player.sendMessage("Removed the prefix from the player.");
                minetopiaPlayer.removePrefix(prefix);
                return;
            }
        }
        player.sendMessage(ChatUtils.color("<red>This player does not have this prefix."));
    }
}
