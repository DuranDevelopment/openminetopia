package nl.openminetopia.modules.player.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.player.utils.PlaytimeUtil;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("time|playtime")
public class PlaytimeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onPlaytimeCommand(Player player, @Optional OfflinePlayer target) {
        if (target != null && player.hasPermission("openminetopia.playtime.others")) {
            MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(target);
            if (minetopiaPlayer == null) return;

            player.sendMessage(ChatUtils.color("<dark_aqua>De speeltijd van " + target.getName() + " is momenteel " + PlaytimeUtil.formatPlaytime(minetopiaPlayer.getPlaytime())));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;

        player.sendMessage(ChatUtils.color("<dark_aqua>Jouw huidige speeltijd is momenteel " + PlaytimeUtil.formatPlaytime(minetopiaPlayer.getPlaytime())));
    }
}
