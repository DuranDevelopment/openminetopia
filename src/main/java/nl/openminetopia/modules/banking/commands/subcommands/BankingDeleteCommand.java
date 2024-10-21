package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("accounts|account|rekening")
public class BankingDeleteCommand extends BaseCommand {

    @Subcommand("delete")
    @Syntax("<name>")
    @CommandCompletion("@accountNames")
    @CommandPermission("openminetopia.banking.delete")
    public void deleteAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_not_found"));
            return;
        }

        dataModule.getAdapter().deleteBankAccount(accountModel.getUniqueId()).whenComplete((v, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(MessageConfiguration.component("banking_account_deletion_error"));
                return;
            }

            // TODO: Replace <account_name> with the actual value
            sender.sendMessage(MessageConfiguration.component("banking_account_deleted"));
            bankingModule.getBankAccountModels().remove(accountModel);
        });
    }

}
