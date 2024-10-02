package nl.openminetopia.modules.prefix.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.modules.prefix.menu.PrefixMenu;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("prefix")
public class PrefixMenuCommand extends BaseCommand {

    @Subcommand("menu")
    @Description("Open het prefix menu.")
    @CommandCompletion("@players")
    public void menuCommand(Player player, @Optional OfflinePlayer target) {
        if (target == null) {
            target = player;
        }

        // Open het prefix menu
        PrefixMenu menu = new PrefixMenu(player, target);
        menu.open(player);
    }
}
