package nl.openminetopia.modules.mod.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.OnlineMinetopiaPlayer;
import nl.openminetopia.api.player.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;

@CommandAlias("mod")
public class ModSetLevelCommand extends BaseCommand {

    @Subcommand("setlevel")
    @Syntax("<player> <level>")
    @Description("Set the level of a player.")
    public static void setLevelCommand(Player player, OfflinePlayer offlinePlayer, int newLevel) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage("This player does not exist.");
            return;
        }

        OnlineMinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;
        int oldLevel = minetopiaPlayer.getLevel();
        minetopiaPlayer.setLevel(newLevel);

        player.sendMessage("Set the level of the player to " + newLevel + ".");

        Bukkit.getServer().getPluginManager().callEvent(new PlayerLevelChangeEvent(offlinePlayer.getPlayer(), oldLevel, newLevel));
    }
}
