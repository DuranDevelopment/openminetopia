package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import nl.openminetopia.modules.banking.menu.BankTypeSelectionMenu;
import org.bukkit.entity.Player;

@CommandAlias("accounts|account|rekening")
public class BankingOpenCommand extends BaseCommand {

    @Default
    public void openBank(Player player) {
        new BankTypeSelectionMenu(player).open(player);
    }

}
