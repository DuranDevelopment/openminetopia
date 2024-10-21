package nl.openminetopia.modules.player.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("time|playtime")
public class PlaytimeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.playtime")
    @Description("Get your or another player's playtime.")
    public void onPlaytimeCommand(Player player, @Optional OfflinePlayer target) {
        if (target != null && player.hasPermission("openminetopia.playtime.others")) {
            MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(target);
            if (minetopiaPlayer == null) {
                player.sendMessage(MessageConfiguration.component("player_not_found"));
                return;
            }

            // TODO: Replace <playername> and <playtime> with actual values.
            player.sendMessage(MessageConfiguration.component("player_time_other_player"));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.sendMessage(MessageConfiguration.component("database_read_error"));
            return;
        }

        // TODO: Replace <playtime> with actual value.
        player.sendMessage(MessageConfiguration.component("player_time_self"));
    }
}
