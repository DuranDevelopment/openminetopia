package nl.openminetopia.modules.color.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import nl.openminetopia.api.player.PlayerManager;
import nl.openminetopia.api.player.objects.MinetopiaPlayer;
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
    public void onPrefix(Player player, OfflinePlayer offlinePlayer, OwnableColorType type, String color) {
        if (offlinePlayer.getPlayer() == null) {
            player.sendMessage(ChatUtils.color("<red>Deze speler bestaat niet."));
            return;
        }

        MinetopiaPlayer minetopiaPlayer = PlayerManager.getInstance().getMinetopiaPlayer(offlinePlayer.getPlayer());
        if (minetopiaPlayer == null) return;

        switch (type) {
            case PREFIX:
                if (minetopiaPlayer.getColors().stream().anyMatch(prefixColor -> prefixColor.getColor().equalsIgnoreCase(color) && prefixColor.getType() == type)) {
                    player.sendMessage(ChatUtils.color("<red>Deze kleur bestaat al."));
                    return;
                }

                PrefixColor prefixColor = new PrefixColor(color, -1L);
                minetopiaPlayer.addColor(prefixColor);
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(color).append(ChatUtils.color(" kleur <dark_aqua>toegevoegd."))));
                break;

            case CHAT:
                if (minetopiaPlayer.getColors().stream().anyMatch(chatColor -> chatColor.getColor().equalsIgnoreCase(color) && chatColor.getType() == type)) {
                    player.sendMessage(ChatUtils.color("<red>Deze chatkleur bestaat al."));
                    return;
                }

                ChatColor chatColor = new ChatColor(color, -1L);
                minetopiaPlayer.addColor(chatColor);
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(color).append(ChatUtils.color(" chatkleur <dark_aqua>toegevoegd."))));
                break;
            case NAME:
                if (minetopiaPlayer.getColors().stream().anyMatch(nameColor -> nameColor.getColor().equalsIgnoreCase(color) && nameColor.getType() == type)) {
                    player.sendMessage(ChatUtils.color("<red>Deze naamkleur bestaat al."));
                    return;
                }

                NameColor nameColor = new NameColor(color, -1L);
                minetopiaPlayer.addColor(nameColor);
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(color).append(ChatUtils.color(" naamkleur <dark_aqua>toegevoegd."))));
                break;
            case LEVEL:
                if (minetopiaPlayer.getColors().stream().anyMatch(levelColor -> levelColor.getColor().equalsIgnoreCase(color) && levelColor.getType() == type)) {
                    player.sendMessage(ChatUtils.color("<red>Deze levelkleur bestaat al."));
                    return;
                }

                LevelColor levelColor = new LevelColor(color, -1L);
                minetopiaPlayer.addColor(levelColor);
                player.sendMessage(ChatUtils.color("<dark_aqua>Je hebt de ").append(Component.text(color).append(ChatUtils.color(" levelkleur <dark_aqua>toegevoegd."))));
                break;
        }
    }
}
