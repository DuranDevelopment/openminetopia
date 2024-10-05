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
public class BankingInfoCommand extends BaseCommand {

    @Subcommand("info")
    @CommandPermission("openminetopia.banking.info")
    public void infoAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        sender.sendMessage(ChatUtils.color("<gold>Rekening Naam: <red>" + accountModel.getName() + " <gold>(<red>ID: " + accountModel.getId() + "<gold>)"));
        sender.sendMessage(ChatUtils.color("<gold> - Frozen: <red>" + accountModel.getFrozen()));
        sender.sendMessage(ChatUtils.color("<gold> - Balance: <red>" + accountModel.getBalance()));
        sender.sendMessage(ChatUtils.color("<gold> - Users: <red>" + accountModel.getUsers().size()));
    }

}
