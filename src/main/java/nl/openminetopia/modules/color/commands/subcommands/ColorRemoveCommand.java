package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.objects.OwnableColor;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("color")
public class ColorRemoveCommand extends BaseCommand {

    /* This and the add code is horrific */
    @Subcommand("remove")
    @Syntax("<player> <type> <color>")
    @CommandCompletion("@players")
    @CommandPermission("openminetopia.color.remove")
    @Description("Remove a color from a player.")
    public void prefix(Player player, OfflinePlayer offlinePlayer, OwnableColorType type, String draftColor) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage(ChatUtils.color("<red>Deze speler bestaat niet."));
            return;
        }

        final String colorId = draftColor.toLowerCase();
        if (!OpenMinetopia.getColorsConfiguration().exists(colorId)) {
            player.sendMessage(ChatUtils.color("<red>Deze kleur bestaat niet."));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        switch (type) {
            case PREFIX:
                Optional<OwnableColor> prefixColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type)).findAny();
                if (prefixColor.isEmpty()) {
                    player.sendMessage(ChatUtils.color("<red>Deze prefixkleur heeft de speler niet."));
                    return;
                }

                minetopiaPlayer.removeColor(prefixColor.get());
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(colorId).append(ChatUtils.color(" kleur <dark_aqua>verwijderd."))));
                break;

            case CHAT:
                Optional<OwnableColor> chatColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type)).findAny();
                if (chatColor.isEmpty()) {
                    player.sendMessage(ChatUtils.color("<red>Deze chatkleur heeft de speler niet."));
                    return;
                }

                minetopiaPlayer.removeColor(chatColor.get());
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(colorId).append(ChatUtils.color(" chatkleur <dark_aqua>verwijderd."))));
                break;
            case NAME:
                Optional<OwnableColor> nameColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type)).findAny();
                if (nameColor.isEmpty()) {
                    player.sendMessage(ChatUtils.color("<red>Deze naamkleur heeft de speler niet."));
                    return;
                }

                minetopiaPlayer.removeColor(nameColor.get());
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(colorId).append(ChatUtils.color(" naamkleur <dark_aqua>verwijderd."))));
                break;
            case LEVEL:
                Optional<OwnableColor> levelColor = minetopiaPlayer.getColors().stream()
                        .filter(c -> c.getColorId().equals(colorId) && c.getType().equals(type)).findAny();
                if (levelColor.isEmpty()) {
                    player.sendMessage(ChatUtils.color("<red>Deze levelkleur heeft de speler niet."));
                    return;
                }

                minetopiaPlayer.removeColor(levelColor.get());
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(colorId).append(ChatUtils.color(" levelkleur <dark_aqua>verwijderd."))));
                break;
        }
    }

}
