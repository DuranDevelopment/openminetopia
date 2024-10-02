package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("prefix")
public class PrefixSelectCommand extends BaseCommand {

    @Subcommand("select")
    @Syntax("<id>")
    public void selectPrefix(Player player, int id) {

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.sendMessage("An error occurred while trying to get your player data.");
            return;
        }

        if (id == -1) {
            minetopiaPlayer.setActivePrefix(new Prefix(-1, OpenMinetopia.getDefaultConfiguration().getDefaultPrefix(), -1));
            player.sendMessage("You selected the default prefix!");
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
            player.sendMessage(ChatUtils.format(minetopiaPlayer,"<gray>-1 <dark_gray>- <prefix_color>" + OpenMinetopia.getDefaultConfiguration().getDefaultPrefix()
                    + " <green><hover:show_text:'<green>Sets this prefix as the active prefix'><click:suggest_command:'/prefix select -1'>SELECT</click></hover>"));

            prefixes.forEach(prefix -> player.sendMessage(ChatUtils.format(minetopiaPlayer,"<gray>" + prefix.getId() + " <dark_gray>- <prefix_color>" + prefix.getPrefix()
                    + " <green><hover:show_text:'<green>Sets this prefix as the active prefix'><click:suggest_command:'/prefix select " + prefix.getId() + "'>SELECT</click></hover>"
                    + " <red><hover:show_text:'<red>Removes this prefix from the player'><click:suggest_command:'/prefix remove " + player.getName() + " " + prefix.getId() + "'>REMOVE</click></hover>")));
            return;
        }

        Prefix prefix = prefixes.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        minetopiaPlayer.setActivePrefix(prefix);
        player.sendMessage("You selected prefix with id " + id + "!");
    }
}
