package nl.openminetopia.modules.places.commands.mtcity;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;

@CommandAlias("mtcity")
public class MTCityCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }
}
