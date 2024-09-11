package nl.openminetopia.modules.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("mod")
public class ModCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage(ChatUtils.color("Help help help!!!"));
        help.showHelp();
    }
}
