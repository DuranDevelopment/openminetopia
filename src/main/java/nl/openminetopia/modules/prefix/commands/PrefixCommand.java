package nl.openminetopia.modules.prefix.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.prefix.menu.PrefixMenu;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("prefix")
public class PrefixCommand extends BaseCommand {

    @HelpCommand
    public void helpCommand(CommandHelp help) {
        help.showHelp();
    }

    @Default
    public void prefixCommand(Player player, CommandHelp help) {

        if (!player.hasPermission("openminetopia.prefix.*")) {
            PrefixMenu menu = new PrefixMenu(player, player);
            menu.open(player);
            return;
        }

        help.showHelp();
    }
}
