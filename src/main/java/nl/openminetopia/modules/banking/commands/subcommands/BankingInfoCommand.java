package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("accounts|account|rekening")
public class BankingInfoCommand extends BaseCommand {

    @Subcommand("info")
    @CommandCompletion("@accountNames")
    @CommandPermission("openminetopia.banking.info")
    public void infoAccount(CommandSender sender, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_not_found"));
            return;
        }

        // TODO: Replace <account_name> <account_id> <account_frozen> <account_balance> <account_users> with the actual values
        // TODO: When boolean is used, show yes or no component.
        sender.sendMessage(MessageConfiguration.component("banking_account_info_line1"));
        sender.sendMessage(MessageConfiguration.component("banking_account_info_line2"));
        sender.sendMessage(MessageConfiguration.component("banking_account_info_line3"));
        sender.sendMessage(MessageConfiguration.component("banking_account_info_line4"));

    }

}
