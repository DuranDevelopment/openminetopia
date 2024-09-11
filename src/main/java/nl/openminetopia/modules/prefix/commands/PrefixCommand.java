package nl.openminetopia.modules.prefix.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;

@CommandAlias("prefix")
public class PrefixCommand extends BaseCommand {
    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }
}
