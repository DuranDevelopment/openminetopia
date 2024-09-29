package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.storm.models.BankPermissionModel;
import nl.openminetopia.utils.ChatUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

@CommandAlias("accounts|account|rekening")
public class BankingUsersCommand extends BaseCommand {

    @Subcommand("adduser")
    @CommandPermission("horizon.accounts.user.adduser")
    @Syntax("<player> <naam> <permission>")
    public void addUser(CommandSender sender, OfflinePlayer target, String accountName, AccountPermission permission) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        BankAccountModel accountModel = bankingModule.getAccountByName(accountName);

        if (accountModel == null) {
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        if (accountModel.getUsers().containsKey(target.getUniqueId())) {
            sender.sendMessage(ChatUtils.color("<red>" + target.getName() + " is al toegevoegd op deze rekening."));
            return;
        }

        accountModel.getUsers().put(target.getUniqueId(), permission);

        BankPermissionModel permissionModel = new BankPermissionModel();
        permissionModel.setUuid(target.getUniqueId());
        permissionModel.setAccount(accountModel.getUniqueId());
        permissionModel.setPermission(permission);

        try {
            bankingModule.createPermissions(permissionModel);
            sender.sendMessage(ChatUtils.color("<gold>Je hebt</gold> <red>" + target.getName() + "</red> <gold>toegevoegd aan de rekening </gold> <red>" + accountName + "</red> <gold>met de rechten <red>" + permission + "</red><gold>."));
        } catch (SQLException e) {
            sender.sendMessage(ChatUtils.color("<red>Er is iets mis gegaan met het aanmaken van de permissie data."));
        }
    }

    @Subcommand("removeuser")
    @CommandPermission("horizon.accounts.user.removeuser")
    @Syntax("<player> <naam>")
    public void removeUser(CommandSender sender, OfflinePlayer target, String accountName) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
    }

}
