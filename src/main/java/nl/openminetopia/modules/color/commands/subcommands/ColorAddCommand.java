package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.google.protobuf.Message;
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

@CommandAlias("color")
public class ColorAddCommand extends BaseCommand {

    @Subcommand("add")
    @Syntax("<player> <type> <color>")
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.color.add")
    @Description("Add a color to a player.")
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
                if (minetopiaPlayer.getColors().stream().anyMatch(prefixColor -> prefixColor.getColorId().equalsIgnoreCase(colorId) && prefixColor.getType() == type)) {
                    player.sendMessage(MessageConfiguration.component("color_prefix_exists"));
                    return;
                }

                PrefixColor prefixColor = new PrefixColor(colorId, -1L);
                minetopiaPlayer.addColor(prefixColor);
                // TODO: Replace <color> with actual color
                // OLD: player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(colorId).append(ChatUtils.color(" kleur <dark_aqua>toegevoegd."))));
                player.sendMessage(MessageConfiguration.component("color_prefix_added"));
                break;

            case CHAT:
                if (minetopiaPlayer.getColors().stream().anyMatch(chatColor -> chatColor.getColorId().equalsIgnoreCase(colorId) && chatColor.getType() == type)) {
                    player.sendMessage(MessageConfiguration.component("color_chat_exists"));
                    return;
                }

                ChatColor chatColor = new ChatColor(colorId, -1L);
                minetopiaPlayer.addColor(chatColor);
                // TODO: Replace <color> with actual color
                player.sendMessage(MessageConfiguration.component("color_chat_added"));
                break;
            case NAME:
                if (minetopiaPlayer.getColors().stream().anyMatch(nameColor -> nameColor.getColorId().equalsIgnoreCase(colorId) && nameColor.getType() == type)) {
                    player.sendMessage(MessageConfiguration.component("color_name_exists"));
                    return;
                }

                NameColor nameColor = new NameColor(colorId, -1L);
                minetopiaPlayer.addColor(nameColor);
                // TODO: Replace <color> with actual color
                player.sendMessage(MessageConfiguration.component("color_name_added"));
                break;
            case LEVEL:
                if (minetopiaPlayer.getColors().stream().anyMatch(levelColor -> levelColor.getColorId().equalsIgnoreCase(colorId) && levelColor.getType() == type)) {
                    player.sendMessage(MessageConfiguration.component("color_level_exists"));
                    return;
                }

                LevelColor levelColor = new LevelColor(colorId, -1L);
                minetopiaPlayer.addColor(levelColor);
                // TODO: Replace <color> with actual color
                player.sendMessage(MessageConfiguration.component("color_level_added"));

                break;
        }
    }
}
