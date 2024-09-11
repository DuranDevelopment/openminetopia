package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.prefix.objects.Prefix;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("prefix")
public class PrefixSelectCommand extends BaseCommand {

    @Subcommand("select")
    @Syntax("<id>")
    public void onSelectCommand(Player player, int id) {

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.sendMessage("An error occurred while trying to get your player data.");
            return;
        }

        List<Prefix> prefixes = minetopiaPlayer.getPrefixes();
        if (prefixes == null) {
            player.sendMessage("An error occurred while trying to get your prefixes.");
            return;
        }

        if (prefixes.stream().noneMatch(prefix -> prefix.getId() == id)) {
            player.sendMessage("You don't have a prefix with id " + id + "!");
            // send available prefixes:
            player.sendMessage("Available prefixes:");
            prefixes.forEach(prefix -> player.sendMessage(prefix.getId() + " - " + prefix.getPrefix()));
            return;
        }

        Prefix prefix = prefixes.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        minetopiaPlayer.setActivePrefix(prefix);
        player.sendMessage("You selected prefix with id " + id + "!");
    }
}
