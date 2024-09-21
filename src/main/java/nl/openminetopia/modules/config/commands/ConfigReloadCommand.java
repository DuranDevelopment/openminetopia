package nl.openminetopia.modules.config.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.entity.Player;

@CommandAlias("openminetopia")
public class ConfigReloadCommand extends BaseCommand {

    @Subcommand("reload")
    public void onReload(Player player) {
        OpenMinetopia.getDefaultConfiguration().reload();
        player.sendMessage(ChatUtils.color(OpenMinetopia.getDefaultConfiguration().getDefaultPrefixColor() + "aaaa"));
    }
}
