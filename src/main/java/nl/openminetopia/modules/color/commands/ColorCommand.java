package nl.openminetopia.modules.color.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import nl.openminetopia.modules.color.enums.OwnableColorType;
import nl.openminetopia.modules.color.menus.ColorSelectMenu;
import nl.openminetopia.modules.color.menus.ColorTypeMenu;
import org.bukkit.entity.Player;

@CommandAlias("color")
public class ColorCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Default
    public void onDefault(Player player) {
        new ColorTypeMenu(player, player).open(player);
    }


    @CommandAlias("prefixcolor|prefixkleur")
    public void onPrefixColor(Player player) {
        new ColorSelectMenu(player, player, OwnableColorType.PREFIX).open(player);
    }

    @CommandAlias("chatcolor|chatkleur")
    public void onChatColor(Player player) {
        new ColorSelectMenu(player, player, OwnableColorType.CHAT).open(player);
    }

    @CommandAlias("namecolor|naamkleur")
    public void onNameColor(Player player) {
        new ColorSelectMenu(player, player, OwnableColorType.NAME).open(player);
    }

    @CommandAlias("levelcolor|levelkleur")
    public void onLevelColor(Player player) {
        new ColorSelectMenu(player, player, OwnableColorType.LEVEL).open(player);
    }
}
