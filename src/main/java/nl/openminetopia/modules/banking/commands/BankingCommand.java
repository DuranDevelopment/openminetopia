package nl.openminetopia.modules.banking.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import nl.openminetopia.OpenMinetopia;
import nl.openminetopia.modules.banking.BankingModule;
import nl.openminetopia.modules.banking.enums.AccountPermission;
import nl.openminetopia.modules.banking.enums.AccountType;
import nl.openminetopia.modules.data.storm.StormDatabase;
import nl.openminetopia.modules.data.storm.models.BankAccountModel;
import nl.openminetopia.modules.data.storm.models.BankPermissionModel;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("accounts|account|rekening")
public class BankingCommand extends BaseCommand {

    @Subcommand("test")
    @Syntax("<type> <name> <balance>")
    public void test(Player player, AccountType type, String name, long balance) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);
        player.sendMessage("executing.");

        BankAccountModel bankAccountModel = new BankAccountModel();
        bankAccountModel.setUniqueId(UUID.randomUUID());
        bankAccountModel.setBalance(balance);
        bankAccountModel.setName(name);
        bankAccountModel.setType(type);
        bankingModule.getBankAccountModels().add(bankAccountModel);
        StormDatabase.getInstance().saveStormModel(bankAccountModel);

        BankPermissionModel permissionModel = new BankPermissionModel();
        permissionModel.setPermission(AccountPermission.ADMIN);
        permissionModel.setAccount(bankAccountModel.getUniqueId());
        permissionModel.setUuid(player.getUniqueId());
        bankAccountModel.getUsers().put(permissionModel.getUuid(), permissionModel.getPermission());
        StormDatabase.getInstance().saveStormModel(permissionModel);
    }

    @Subcommand("listAll")
    public void listAll(CommandSender sender) {
        BankingModule bankingModule = OpenMinetopia.getModuleManager().getModule(BankingModule.class);

        for (BankAccountModel bankAccountModel : bankingModule.getBankAccountModels()) {
            sender.sendMessage(bankAccountModel.getName());
            sender.sendMessage("  - Balance:" + bankAccountModel.getBalance());
            sender.sendMessage("  - Users:");

            bankAccountModel.getUsers().forEach((uuid, permission) -> {
                sender.sendMessage("    " + uuid + ": " + permission);
            });
        }

    }

}
