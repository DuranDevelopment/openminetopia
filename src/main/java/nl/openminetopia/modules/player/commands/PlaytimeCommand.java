package nl.openminetopia.modules.player.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

public class PlaytimeCommand extends BaseCommand {

    @CommandAlias("playtime")
    public void onPlaytimeCommand(Player player) {
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) return;
        int playtimeInSeconds = minetopiaPlayer.getPlaytime();
        int days = playtimeInSeconds / 86400;
        int hours = (playtimeInSeconds % 86400) / 3600;
        int minutes = ((playtimeInSeconds % 86400) % 3600) / 60;

        player.sendMessage(ChatUtils.color("<dark_aqua>Jouw huidige speeltijd is momenteel <aqua>" + days + " <dark_aqua>dagen, <aqua>" + hours + " <dark_aqua>uren, <aqua>"+ minutes + " <dark_aqua>minuten, <aqua>" + playtimeInSeconds + " <dark_aqua>seconden."));
    }
}
