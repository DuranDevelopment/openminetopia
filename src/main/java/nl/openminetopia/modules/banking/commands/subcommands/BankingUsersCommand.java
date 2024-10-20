package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.configuration.MessageConfiguration;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.data.DataModule;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@CommandAlias("accounts|account|rekening")
public class BankingUsersCommand extends BaseCommand {

    @Subcommand("adduser")
    @CommandPermission("openminetopia.banking.adduser")
    @Syntax("<player> <naam> <permission>")
    @CommandCompletion("@players @accountNames")
    public void addUser(CommandSender sender, OfflinePlayer target, String accountName, AccountPermission permission) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_not_found"));
            return;
        }

        if (accountModel.getUsers().containsKey(target.getUniqueId())) {
            // TODO: Replace <player_name> with the actual value
            sender.sendMessage(MessageConfiguration.component("banking_user_already_exists"));
            return;
        }

        dataModule.getAdapter().createBankPermission(target.getUniqueId(), accountModel.getUniqueId(), permission).whenComplete(((permissionModel, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(MessageConfiguration.component("database_update_error"));
                return;
            }

            accountModel.getUsers().put(target.getUniqueId(), permission);
            // TODO: Replace <player_name> <account_name> <permission> with the actual values
            sender.sendMessage(MessageConfiguration.component("banking_user_added"));
        }));

    }

    @Subcommand("removeuser")
    @CommandPermission("openminetopia.banking.removeuser")
    @Syntax("<player> <naam>")
    @CommandCompletion("@players @accountNames")
    public void removeUser(CommandSender sender, OfflinePlayer target, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        DataModule dataModule = OpenMinetopia.getModuleManager().getModule(DataModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(MessageConfiguration.component("banking_account_not_found"));
            return;
        }

        dataModule.getAdapter().deleteBankPermission(accountModel.getUniqueId(), target.getUniqueId()).whenComplete((v, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(MessageConfiguration.component("database_update_error"));
                return;
            }

            accountModel.getUsers().remove(target.getUniqueId());
            // TODO: Replace <player_name> <account_name> with the actual values
            sender.sendMessage(ChatUtils.color("<gold>Je hebt</gold> <red>" + target.getName() + "</red> <gold>verwijderd van de de rekening</gold> <red>" + accountModel.getName() + "</red><gold>."));
        });
    }

}
