package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.modules.prefix.objects.Prefix;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("color")
public class ColorSelectCommand extends BaseCommand {

    @Subcommand("select")
    public void onPrefix(CommandSender sender, OwnableColorType ownableColorType, int id) {

        Player player = (Player) sender;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.sendMessage("An error occurred while trying to get your player data.");
            return;
        }

        switch (ownableColorType) {
            case PREFIX:
                if (id == -1) {
                    minetopiaPlayer.setActivePrefixColor(new PrefixColor(-1, OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor(), -1));
                    player.sendMessage("You selected the default prefix color!");
                    return;
                }
                List<PrefixColor> prefixColors = minetopiaPlayer.getPrefixColors();
                if (prefixColors == null) {
                    player.sendMessage("An error occurred while trying to get your prefix colors.");
                    return;
                }
                if (prefixColors.stream().noneMatch(color -> color.getId() == id)) {
                    player.sendMessage("You don't have a prefix color with id " + id + "!");
                    // send available prefixes:
                    player.sendMessage("Available prefix colors:");
                    player.sendMessage("-1 - " + OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor().replace("<", "").replace(">", ""));
                    prefixColors.forEach(color -> player.sendMessage(color.getId() + " - " + color.getColor().replace("<", "").replace(">", "")));
                    return;
                }

                PrefixColor prefixColor = prefixColors.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
                minetopiaPlayer.setActivePrefixColor(prefixColor);
                player.sendMessage("You selected prefix color with id " + id + "!");
                break;
            case CHAT:
                break;
            case NAME:
                break;
            case LEVEL:
                break;
        }

    }

}
