package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.ChatColor;
import nl.openminetopia.modules.color.objects.LevelColor;
import nl.openminetopia.modules.color.objects.NameColor;
import nl.openminetopia.modules.color.objects.PrefixColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@CommandAlias("color")
public class ColorSelectCommand extends BaseCommand {

    @Subcommand("select")
    public void onPrefix(CommandSender sender, OwnableColorType ownableColorType, String colorName) {

        Player player = (Player) sender;
        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(player);
        if (minetopiaPlayer == null) {
            player.sendMessage("An error occurred while trying to get your player data.");
            return;
        }

        final String draftColor = colorName.toLowerCase();
        switch (ownableColorType) {
            case PREFIX:
                if (draftColor.equals("default")) {
                    minetopiaPlayer.setActiveColor(new PrefixColor(-1, OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor(), -1), OwnableColorType.PREFIX);
                    player.sendMessage("You selected the default prefix color!");
                    return;
                }
                List<PrefixColor> prefixColors = minetopiaPlayer.getColors().stream()
                        .filter(color -> color instanceof PrefixColor)
                        .map(color -> (PrefixColor) color)
                        .toList();
                if (prefixColors.stream().noneMatch(color -> color.getColor().equals(draftColor))) {
                    player.sendMessage("You don't have a prefix color with id " + draftColor + "!");
                    // send available prefixes:
                    player.sendMessage("Available prefix colors:");
                    player.sendMessage("Default - " + OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor().replace("<", "").replace(">", ""));
                    prefixColors.forEach(color -> player.sendMessage(color.getId() + " - " + color.getColor()));
                    return;
                }

                PrefixColor prefixColor = prefixColors.stream().filter(p -> p.getColor().equals(draftColor)).findFirst().orElse(null);
                minetopiaPlayer.setActiveColor(prefixColor, OwnableColorType.PREFIX);
                player.sendMessage("You selected prefix color with id " + draftColor + "!");
                break;

            case CHAT:
                if (draftColor.equals("default")) {
                    minetopiaPlayer.setActiveColor(new ChatColor(-1, OpenMinetopia.getDefaultConfiguration().getDefaultChatColor(), -1), OwnableColorType.CHAT);
                    player.sendMessage("You selected the default chat color!");
                    return;
                }
                List<ChatColor> chatColors = minetopiaPlayer.getColors().stream()
                        .filter(color -> color instanceof ChatColor)
                        .map(color -> (ChatColor) color)
                        .toList();
                if (chatColors.stream().noneMatch(color -> color.getColor().equals(draftColor))) {
                    player.sendMessage("You don't have a chat color with id " + draftColor + "!");
                    // send available chat colors:
                    player.sendMessage("Available prefix colors:");
                    player.sendMessage("Default - " + OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor().replace("<", "").replace(">", ""));
                    chatColors.forEach(color -> player.sendMessage(color.getId() + " - " + color.getColor()));
                    return;
                }

                ChatColor chatColor = chatColors.stream().filter(c -> c.getColor().equals(draftColor)).findFirst().orElse(null);
                minetopiaPlayer.setActiveColor(chatColor, OwnableColorType.CHAT);
                player.sendMessage("You selected chat color with id " + draftColor + "!");
                break;

            case NAME:
                if (draftColor.equals("default")) {
                    minetopiaPlayer.setActiveColor(new NameColor(-1, OpenMinetopia.getDefaultConfiguration().getDefaultNameColor(), -1), OwnableColorType.NAME);
                    player.sendMessage("You selected the default name color!");
                    return;
                }
                List<NameColor> nameColors = minetopiaPlayer.getColors().stream()
                        .filter(color -> color instanceof NameColor)
                        .map(color -> (NameColor) color)
                        .toList();
                if (nameColors.stream().noneMatch(color -> color.getColor().equals(draftColor))) {
                    player.sendMessage("You don't have a name color with id " + draftColor + "!");
                    // send available name colors:
                    player.sendMessage("Available prefix colors:");
                    player.sendMessage("Default - " + OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor().replace("<", "").replace(">", ""));
                    nameColors.forEach(color -> player.sendMessage(color.getId() + " - " + color.getColor()));
                    return;
                }

                NameColor nameColor = nameColors.stream().filter(n -> n.getColor().equals(draftColor)).findFirst().orElse(null);
                minetopiaPlayer.setActiveColor(nameColor, OwnableColorType.NAME);
                player.sendMessage("You selected name color with id " + draftColor + "!");
                break;

            case LEVEL:
                if (draftColor.equals("default")) {
                    minetopiaPlayer.setActiveColor(new LevelColor(-1, OpenMinetopia.getDefaultConfiguration().getDefaultLevelColor(), -1), OwnableColorType.LEVEL);
                    player.sendMessage("You selected the default level color!");
                    return;
                }
                List<LevelColor> levelColors = minetopiaPlayer.getColors().stream()
                        .filter(color -> color instanceof LevelColor)
                        .map(color -> (LevelColor) color)
                        .toList();
                if (levelColors.stream().noneMatch(color -> color.getColor().equals(draftColor))) {
                    player.sendMessage("You don't have a level color with id " + draftColor + "!");
                    // send available level colors:
                    player.sendMessage("Available prefix colors:");
                    player.sendMessage("Default - " + OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor().replace("<", "").replace(">", ""));
                    levelColors.forEach(color -> player.sendMessage(color.getId() + " - " + color.getColor()));
                    return;
                }

                LevelColor levelColor = levelColors.stream().filter(l -> l.getColor().equals(draftColor)).findFirst().orElse(null);
                minetopiaPlayer.setActiveColor(levelColor, OwnableColorType.LEVEL);
                player.sendMessage("You selected level color with id " + draftColor + "!");
                break;
        }


    }

}
