package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.ChatColor;
import nl.openminetopia.modules.color.objects.LevelColor;
import nl.openminetopia.modules.color.objects.NameColor;
import nl.openminetopia.modules.color.objects.PrefixColor;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("color")
public class ColorRemoveCommand extends BaseCommand {

    @Subcommand("remove")
    @Syntax("<player> <type> <color>")
    @CommandCompletion("@players @colorTypes @playerColors")
    @CommandPermission("openminetopia.color.remove")
    @Description("Remove a color from a player.")
    public void prefix(Player player, OfflinePlayer offlinePlayer, OwnableColorType type, String draftColor) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage(MessageConfiguration.component("player_not_found"));
            return;
        }

        final String colorId = draftColor.toLowerCase();
        if (!OpenMinetopia.getColorsConfiguration().exists(colorId)) {
            player.sendMessage(MessageConfiguration.component("color_not_found"));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        switch (type) {
            case PREFIX:
                Optional<PrefixColor> prefixColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type))
                        .map(c -> (PrefixColor) c)
                        .findAny();
                if (prefixColor.isEmpty()) {
                    player.sendMessage(MessageConfiguration.component("color_prefix_not_found"));
                    return;
                }

                minetopiaPlayer.removeColor(prefixColor.get());
                // TODO: Replace <color> with the actual color
                player.sendMessage(MessageConfiguration.component("color_prefix_removed"));
                break;

            case CHAT:
                Optional<ChatColor> chatColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type))
                        .map(c -> (ChatColor) c)
                        .findAny();
                if (chatColor.isEmpty()) {
                    player.sendMessage(MessageConfiguration.component("color_chat_not_found"));
                    return;
                }

                minetopiaPlayer.removeColor(chatColor.get());
                // TODO: Replace <color> with the actual color
                player.sendMessage(MessageConfiguration.component("color_chat_removed"));
                break;

            case NAME:
                Optional<NameColor> nameColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type))
                        .map(c -> (NameColor) c)
                        .findAny();
                if (nameColor.isEmpty()) {
                    player.sendMessage(MessageConfiguration.component("color_name_not_found"));
                    return;
                }

                minetopiaPlayer.removeColor(nameColor.get());
                // TODO: Replace <color> with the actual color
                player.sendMessage(MessageConfiguration.component("color_name_removed"));
                break;

            case LEVEL:
                Optional<LevelColor> levelColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type))
                        .map(c -> (LevelColor) c)
                        .findAny();
                if (levelColor.isEmpty()) {
                    player.sendMessage(MessageConfiguration.component("color_level_not_found"));
                    return;
                }

                minetopiaPlayer.removeColor(levelColor.get());
                // TODO: Replace <color> with the actual color
                player.sendMessage(MessageConfiguration.component("color_level_removed"));
                break;
        }
    }

}
