package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("accounts|account|rekening")
public class BankingBalanceCommand extends BaseCommand {

    @Subcommand("setbalance")
    @CommandPermission("openminetopia.banking.setbalance")
    public void setBalance(CommandSender sender, String accountName, double balance) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        accountModel.setBalance(balance);
        sender.sendMessage(ChatUtils.color("<gold>De balans van <red>" + accountModel.getName() + " <gold>is nu ingesteld op <red>" + balance + "<gold>."));
    }

}
