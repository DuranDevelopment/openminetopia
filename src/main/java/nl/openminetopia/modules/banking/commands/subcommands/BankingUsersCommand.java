package nl.openminetopia.modules.banking.commands.subcommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.openminetopia.OpenMinetopia;
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
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        if (accountModel.getUsers().containsKey(target.getUniqueId())) {
            sender.sendMessage(ChatUtils.color("<red>" + target.getName() + " is al toegevoegd op deze rekening."));
            return;
        }

        dataModule.getAdapter().createBankPermission(target.getUniqueId(), accountModel.getUniqueId(), permission).whenComplete(((permissionModel, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(ChatUtils.color("<red>Er is iets mis gegaan met het aanmaken van de permissie data."));
                return;
            }

            accountModel.getUsers().put(target.getUniqueId(), permission);
            sender.sendMessage(ChatUtils.color("<gold>Je hebt</gold> <red>" + target.getName() + "</red> <gold>toegevoegd aan de rekening</gold> <red>" + accountName + "</red> <gold>met de rechten</gold> <red>" + permission + "</red><gold>."));
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
            sender.sendMessage(ChatUtils.color("<red>Er is geen account gevonden met deze naam."));
            return;
        }

        dataModule.getAdapter().deleteBankPermission(accountModel.getUniqueId(), target.getUniqueId()).whenComplete((v, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(ChatUtils.color("<red>Er is iets mis gegaan met het verwijderen van de speler."));
                return;
            }

            accountModel.getUsers().remove(target.getUniqueId());
            sender.sendMessage(ChatUtils.color("<gold>Je hebt</gold> <red>" + target.getName() + "</red> <gold>verwijderd van de de rekening</gold> <red>" + accountModel.getName() + "</red><gold>."));
        });
    }

}
